package com.vtms.nirapod.ui.tax;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.AitRecord;
import java.util.*;
import java.util.Locale;

public class AitAdapter extends RecyclerView.Adapter<AitAdapter.ViewHolder> {

    private List<AitRecord> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AitRecord item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<AitRecord> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ait, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int p) {
        AitRecord item = items.get(p);
        h.tvTaxpayerName.setText(item.getTaxpayerName());
        h.tvRefNo.setText(item.getAitReferenceNo());
        h.tvStatus.setText(item.getStatus().replace("_", " "));
        h.tvTin.setText(item.getTaxpayerTin());
        h.tvSource.setText(item.getSourceType().replace("_", " "));
        h.tvYear.setText(item.getFiscalYearName());
        h.tvTaxableValue.setText(format(item.getTaxableValue()));
        h.tvAitAmount.setText(format(item.getCalculatedAitAmount()));
        h.tvChallan.setText(item.getChallanNumber() != null ? "✔ " + item.getChallanNumber() : "—");
        h.tvDate.setText(item.getCreatedAt());

        // Color status
        int color;
        String s = item.getStatus();
        if (s.equals("APPROVED") || s.equals("CREDITED")) {
            color = h.itemView.getContext().getColor(R.color.success);
        } else if (s.equals("REJECTED")) {
            color = h.itemView.getContext().getColor(R.color.error);
        } else {
            color = h.itemView.getContext().getColor(R.color.primary);
        }
        h.tvStatus.getBackground().setTint(color);

        h.btnView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    private String format(double amount) {
        return String.format(Locale.US, "৳%,.2f", amount);
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaxpayerName, tvRefNo, tvStatus, tvTin, tvSource, tvYear, tvTaxableValue, tvAitAmount, tvChallan, tvDate;
        View btnView;
        ViewHolder(View v) {
            super(v);
            tvTaxpayerName = v.findViewById(R.id.tv_taxpayer_name);
            tvRefNo = v.findViewById(R.id.tv_ref_no);
            tvStatus = v.findViewById(R.id.tv_status);
            tvTin = v.findViewById(R.id.tv_tin);
            tvSource = v.findViewById(R.id.tv_source);
            tvYear = v.findViewById(R.id.tv_year);
            tvTaxableValue = v.findViewById(R.id.tv_taxable_value);
            tvAitAmount = v.findViewById(R.id.tv_ait_amount);
            tvChallan = v.findViewById(R.id.tv_challan);
            tvDate = v.findViewById(R.id.tv_date);
            btnView = v.findViewById(R.id.btn_view);
        }
    }
}
