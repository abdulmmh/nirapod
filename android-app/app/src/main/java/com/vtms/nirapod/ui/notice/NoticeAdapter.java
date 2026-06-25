package com.vtms.nirapod.ui.notice;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.Notice;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeVH> {

    private List<Notice> items = new ArrayList<>();
    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(Notice notice);
    }

    public void setListener(OnClickListener l) { this.listener = l; }

    public void setItems(List<Notice> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public NoticeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeVH h, int position) {
        h.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class NoticeVH extends RecyclerView.ViewHolder {
        TextView tvSubject, tvNoticeNo, tvPriority, tvReadStatus, tvSnippet, tvSender, tvDate;
        View vUnreadDot;
        View btnView;

        NoticeVH(View v) {
            super(v);
            tvSubject    = v.findViewById(R.id.tv_subject);
            tvNoticeNo   = v.findViewById(R.id.tv_notice_no);
            tvPriority   = v.findViewById(R.id.tv_priority);
            tvReadStatus = v.findViewById(R.id.tv_read_status);
            tvSnippet    = v.findViewById(R.id.tv_snippet);
            tvSender     = v.findViewById(R.id.tv_sender);
            tvDate       = v.findViewById(R.id.tv_date);
            vUnreadDot   = v.findViewById(R.id.v_unread_dot);
            btnView      = v.findViewById(R.id.btn_view);
        }

        void bind(Notice n) {
            tvSubject.setText(n.getSubject());
            tvNoticeNo.setText(n.getNoticeNo());
            tvSnippet.setText(n.getBody());
            tvSender.setText(n.getIssuedBy() != null ? n.getIssuedBy() : "System");
            tvDate.setText(n.getCreatedAt() != null && n.getCreatedAt().length() >= 10 ? n.getCreatedAt().substring(0, 10) : "---");

            // Priority logic
            String priority = n.getPriority() != null ? n.getPriority() : "NORMAL";
            tvPriority.setText(priority);
            if ("HIGH".equalsIgnoreCase(priority)) {
                tvPriority.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FEF2F2")));
                tvPriority.setTextColor(Color.parseColor("#EF4444"));
            } else {
                tvPriority.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF6FF")));
                tvPriority.setTextColor(Color.parseColor("#2563EB"));
            }

            // Read status
            if (!n.isRead()) {
                tvReadStatus.setText("Unread");
                tvReadStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#EFF6FF")));
                tvReadStatus.setTextColor(Color.parseColor("#2563EB"));
                vUnreadDot.setVisibility(View.VISIBLE);
            } else {
                tvReadStatus.setText("Read");
                tvReadStatus.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F1F5F9")));
                tvReadStatus.setTextColor(Color.parseColor("#64748B"));
                vUnreadDot.setVisibility(View.GONE);
            }

            btnView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(n);
            });
            
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(n);
            });
        }
    }
}
