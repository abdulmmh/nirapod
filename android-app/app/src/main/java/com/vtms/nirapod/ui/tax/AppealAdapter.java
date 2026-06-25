package com.vtms.nirapod.ui.tax;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.Appeal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppealAdapter extends RecyclerView.Adapter<AppealAdapter.VH> {

    private List<Appeal> items = new ArrayList<>();
    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(Appeal appeal);
    }

    public void setListener(OnClickListener l) { this.listener = l; }

    public void setItems(List<Appeal> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appeal, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        h.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class VH extends RecyclerView.ViewHolder {
        TextView tvNo, tvStatus, tvType, tvDemanded, tvDisputed, tvFiledOn;
        VH(View v) {
            super(v);
            tvNo       = v.findViewById(R.id.tvAppealNo);
            tvStatus   = v.findViewById(R.id.tvStatus);
            tvType     = v.findViewById(R.id.tvLabelType);
            tvDemanded = v.findViewById(R.id.tvDemanded);
            tvDisputed = v.findViewById(R.id.tvDisputed);
            tvFiledOn  = v.findViewById(R.id.tvFiledOn);
        }

        void bind(Appeal a) {
            tvNo.setText(a.getAppealNo());
            tvStatus.setText(a.getStatus());
            tvType.setText(a.getAppealType().replace("_", " "));
            tvDemanded.setText(String.format(Locale.US, "৳%,.2f", a.getDemandedAmount()));
            tvDisputed.setText(String.format(Locale.US, "৳%,.2f", a.getDisputedAmount()));
            tvFiledOn.setText(a.getFiledAt() != null && a.getFiledAt().length() >= 10 ? a.getFiledAt().substring(0, 10) : "---");

            int color;
            String status = a.getStatus();
            if ("DECIDED".equals(status) || "CLOSED".equals(status) || "SUCCESS".equals(status)) {
                color = itemView.getContext().getColor(R.color.success);
            } else if ("REJECTED".equals(status) || "FAILED".equals(status)) {
                color = itemView.getContext().getColor(R.color.error);
            } else if ("PENDING".equals(status) || "FILED".equals(status)) {
                color = itemView.getContext().getColor(R.color.warning);
            } else {
                color = itemView.getContext().getColor(R.color.primary);
            }
            
            if (tvStatus.getBackground() != null) {
                tvStatus.getBackground().setTint(color);
            }

            itemView.findViewById(R.id.btn_view).setOnClickListener(v -> {
                if (listener != null) listener.onClick(a);
            });
        }
    }
}
