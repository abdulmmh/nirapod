package com.vtms.nirapod.ui.notice;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Notice;
import com.vtms.nirapod.data.model.NoticeReplyRequest;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class NoticeDetailFragment extends Fragment {

    private Notice notice;
    private TextView tvNoticeNo, tvNoticeType, tvBadgePriority, tvBadgeStatus;
    private TextView tvSubject, tvIssuedBy, tvCreatedAt, tvTaxpayerInfo, tvTargetType, tvBody;
    private TextView tvPrevReply, tvResult;
    private TextInputEditText etReply;
    private MaterialButton btnReply, btnBack;
    private View pbLoading;

    public static NoticeDetailFragment newInstance(Notice notice) {
        NoticeDetailFragment f = new NoticeDetailFragment();
        f.notice = notice;
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        
        // Initial bind
        bindNotice(notice);
        
        // Refresh full notice from API
        loadNoticeDetail();
    }

    private void bindViews(View v) {
        tvNoticeNo      = v.findViewById(R.id.tv_notice_no);
        tvNoticeType    = v.findViewById(R.id.tv_notice_type);
        tvBadgePriority = v.findViewById(R.id.tv_badge_priority);
        tvBadgeStatus   = v.findViewById(R.id.tv_badge_status);
        tvSubject       = v.findViewById(R.id.tv_subject);
        tvIssuedBy      = v.findViewById(R.id.tv_issued_by);
        tvCreatedAt     = v.findViewById(R.id.tv_created_at);
        tvTaxpayerInfo  = v.findViewById(R.id.tv_taxpayer_info);
        tvTargetType    = v.findViewById(R.id.tv_target_type);
        tvBody          = v.findViewById(R.id.tv_body);
        
        tvPrevReply     = v.findViewById(R.id.tv_prev_reply);
        tvResult        = v.findViewById(R.id.tv_result);
        etReply         = v.findViewById(R.id.et_reply);
        btnReply        = v.findViewById(R.id.btn_reply);
        btnBack         = v.findViewById(R.id.btn_back);
        
        btnBack.setOnClickListener(view -> requireActivity().onBackPressed());
        
        btnReply.setOnClickListener(view -> sendReply());
    }

    private void bindNotice(Notice n) {
        if (n == null) return;
        
        tvNoticeNo.setText(n.getNoticeNo() != null ? n.getNoticeNo() : "NTC-PENDING");
        tvNoticeType.setText(n.getNoticeType());
        
        tvSubject.setText(n.getSubject());
        tvBody.setText(n.getBody());
        
        String date = n.getCreatedAt();
        tvCreatedAt.setText("📅 " + (date != null && date.length() >= 10 ? date.substring(0, 10) : "---"));
        
        tvIssuedBy.setText("👤 " + (n.getIssuedBy() != null ? n.getIssuedBy() : "System"));
        
        if (n.getTaxpayerName() != null) {
            tvTaxpayerInfo.setText("🪪 " + n.getTaxpayerName() + " (" + n.getTinNumber() + ")");
            tvTaxpayerInfo.setVisibility(View.VISIBLE);
        } else {
            tvTaxpayerInfo.setVisibility(View.GONE);
        }
        
        tvTargetType.setText("👥 " + (n.getTargetType() != null ? n.getTargetType() : "Specific Taxpayer"));
        
        if (n.getPriority() != null) {
            tvBadgePriority.setText("🚩 " + n.getPriority());
            tvBadgePriority.setVisibility(View.VISIBLE);
        } else {
            tvBadgePriority.setVisibility(View.GONE);
        }
        
        if (n.getStatus() != null) {
            tvBadgeStatus.setText(n.getStatus());
            tvBadgeStatus.setVisibility(View.VISIBLE);
        } else {
            tvBadgeStatus.setVisibility(View.GONE);
        }

        if (n.getResponseText() != null && !n.getResponseText().isEmpty()) {
            tvPrevReply.setText("Previous reply: " + n.getResponseText());
            tvPrevReply.setVisibility(View.VISIBLE);
        } else {
            tvPrevReply.setVisibility(View.GONE);
        }
    }

    private void loadNoticeDetail() {
        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Mark as read
                RetrofitClient.noticeApi(tm).markAsRead(notice.getId()).execute();
                
                // Fetch full detail
                Response<Notice> resp = RetrofitClient.noticeApi(tm)
                        .getNoticeById(notice.getId()).execute();

                requireActivity().runOnUiThread(() -> {
                    if (resp.isSuccessful() && resp.body() != null) {
                        notice = resp.body();
                        bindNotice(notice);
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void sendReply() {
        String reply = etReply.getText().toString().trim();
        if (reply.isEmpty()) {
            Toast.makeText(requireContext(), "Please write a response", Toast.LENGTH_SHORT).show();
            return;
        }

        btnReply.setEnabled(false);
        btnReply.setText("Sending...");
        tvResult.setVisibility(View.GONE);

        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                var resp = RetrofitClient.noticeApi(tm)
                        .replyToNotice(notice.getId(), new NoticeReplyRequest(reply)).execute();

                requireActivity().runOnUiThread(() -> {
                    btnReply.setEnabled(true);
                    btnReply.setText("Send Response");
                    if (resp.isSuccessful()) {
                        tvResult.setText("Response sent successfully!");
                        tvResult.setTextColor(requireContext().getResources().getColor(R.color.success));
                        tvResult.setVisibility(View.VISIBLE);
                        etReply.setText("");
                        // Update local notice object to show prev reply
                        loadNoticeDetail();
                    } else {
                        tvResult.setText("Failed to send response");
                        tvResult.setTextColor(requireContext().getResources().getColor(R.color.error));
                        tvResult.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    btnReply.setEnabled(true);
                    btnReply.setText("Send Response");
                    tvResult.setText("Cannot connect to server");
                    tvResult.setTextColor(requireContext().getResources().getColor(R.color.error));
                    tvResult.setVisibility(View.VISIBLE);
                });
            }
        });
    }
}
