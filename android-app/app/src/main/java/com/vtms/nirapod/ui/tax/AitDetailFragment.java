package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AitRecord;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AitDetailFragment extends Fragment {

    private static final String ARG_AIT_ID = "ait_id";
    private long aitId;
    private AitRecord ait;
    private ProgressBar pbLoading;
    private View layoutContent;
    
    // Status Tracker Views
    private TextView tvStepDraft, tvStepSubmitted, tvStepPending, tvStepReview, tvStepApproved, tvStepCredited;
    private View line1, line2, line3, line4, line5;

    public static AitDetailFragment newInstance(long id) {
        AitDetailFragment f = new AitDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_AIT_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) aitId = getArguments().getLong(ARG_AIT_ID);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ait_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbLoading = view.findViewById(R.id.pb_loading);
        layoutContent = view.findViewById(R.id.layout_content);
        
        bindStatusViews(view);
        loadData();

        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void bindStatusViews(View v) {
        tvStepDraft = v.findViewById(R.id.tv_step_draft);
        tvStepSubmitted = v.findViewById(R.id.tv_step_submitted);
        tvStepPending = v.findViewById(R.id.tv_step_pending);
        tvStepReview = v.findViewById(R.id.tv_step_review);
        tvStepApproved = v.findViewById(R.id.tv_step_approved);
        tvStepCredited = v.findViewById(R.id.tv_step_credited);
    }

    private void loadData() {
        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);
        layoutContent.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // For simplicity using list and filtering for now if specific getById isn't defined yet
                Response<AitRecord> response = RetrofitClient.aitApi(tm).getMyAitRecordById(aitId).execute();
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        ait = response.body();
                        bindData(layoutContent);
                        updateStatusTracker();
                        layoutContent.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void bindData(View v) {
        ((TextView)v.findViewById(R.id.tv_val_name)).setText(ait.getTaxpayerName());
        ((TextView)v.findViewById(R.id.tv_val_tin)).setText(ait.getTaxpayerTin());
        ((TextView)v.findViewById(R.id.tv_val_year)).setText(ait.getFiscalYearName());
        ((TextView)v.findViewById(R.id.tv_val_submitted)).setText(ait.getCreatedAt());

        ((TextView)v.findViewById(R.id.tv_ref_no_title)).setText("AIT Record — " + ait.getAitReferenceNo());
        ((TextView)v.findViewById(R.id.tv_val_source)).setText(ait.getSourceType().replace("_", " "));
        ((TextView)v.findViewById(R.id.tv_val_deductor)).setText(ait.getDeductorName() != null ? ait.getDeductorName() : "—");
        ((TextView)v.findViewById(R.id.tv_val_taxable)).setText(String.format("৳%,.2f", ait.getTaxableValue()));
        ((TextView)v.findViewById(R.id.tv_val_rate)).setText(ait.getAitRate() + "%");
        ((TextView)v.findViewById(R.id.tv_val_ait)).setText(String.format("৳%,.2f", ait.getCalculatedAitAmount()));
        
        ((TextView)v.findViewById(R.id.tv_val_challan)).setText(ait.getChallanNumber() != null ? ait.getChallanNumber() : "—");
        ((TextView)v.findViewById(R.id.tv_val_bank)).setText(ait.getBankName() != null ? ait.getBankName() : "—");
    }

    private void updateStatusTracker() {
        String s = ait.getStatus();
        setStepActive(tvStepDraft, true);
        if (s.equals("DRAFT")) return;
        
        setStepActive(tvStepSubmitted, true);
        if (s.equals("SUBMITTED")) return;
        
        setStepActive(tvStepPending, true);
        if (s.equals("PENDING")) return;
        
        setStepActive(tvStepReview, true);
        if (s.equals("UNDER_REVIEW")) return;
        
        setStepActive(tvStepApproved, true);
        if (s.equals("APPROVED")) return;
        
        setStepActive(tvStepCredited, true);
    }

    private void setStepActive(TextView tv, boolean active) {
        if (active) {
            tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.success)));
            tv.setTextColor(requireContext().getColor(R.color.white));
            tv.setCompoundDrawablesWithIntrinsicBounds(requireContext().getDrawable(R.drawable.ic_check), null, null, null);
        }
    }
}
