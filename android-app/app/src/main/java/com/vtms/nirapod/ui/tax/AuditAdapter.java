package com.vtms.nirapod.ui.tax;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.AuditCase;
import java.util.*;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ViewHolder> {

    private List<AuditCase> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AuditCase item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<AuditCase> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AuditCase item = items.get(position);
        holder.tvCaseNo.setText(item.getCaseNo());
        holder.tvStatus.setText(item.getStatus().replace("_", " "));
        holder.tvMeta.setText(String.format("%s  ·  %s", item.getAuditType(), item.getFiscalYear()));

        // Set status badge color
        int color;
        String s = item.getStatus();
        if (s.contains("APPROVED") || s.contains("SUCCESS") || s.equals("PAID")) {
            color = holder.itemView.getContext().getColor(R.color.success);
        } else if (s.contains("ISSUED") || s.contains("REQUESTED") || s.contains("OPEN")) {
            color = holder.itemView.getContext().getColor(R.color.warning);
        } else if (s.contains("DEMAND") || s.contains("REJECTED")) {
            color = holder.itemView.getContext().getColor(R.color.error);
        } else {
            color = holder.itemView.getContext().getColor(R.color.primary);
        }
        holder.tvStatus.getBackground().setTint(color);

        holder.btnView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCaseNo, tvStatus, tvMeta;
        View btnView;
        ViewHolder(View v) {
            super(v);
            tvCaseNo = v.findViewById(R.id.tv_case_no);
            tvStatus  = v.findViewById(R.id.tv_status);
            tvMeta    = v.findViewById(R.id.tv_meta);
            btnView   = v.findViewById(R.id.btn_view);
        }
    }
}
