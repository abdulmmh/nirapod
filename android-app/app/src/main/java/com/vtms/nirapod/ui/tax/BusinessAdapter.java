package com.vtms.nirapod.ui.tax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.BusinessVatStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.BusinessVH> {

    private List<BusinessVatStatus> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BusinessVatStatus item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<BusinessVatStatus> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public BusinessVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_business, parent, false);
        return new BusinessVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessVH h, int position) {
        h.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class BusinessVH extends RecyclerView.ViewHolder {
        TextView tvName, tvRegNo, tvType, tvDistrict, tvTurnover, tvStatus, tvIcon;
        View avatarBg;

        BusinessVH(View v) {
            super(v);
            tvName     = v.findViewById(R.id.tv_business_name);
            tvRegNo    = v.findViewById(R.id.tv_reg_no);
            tvType     = v.findViewById(R.id.tv_type);
            tvDistrict = v.findViewById(R.id.tv_district);
            tvTurnover = v.findViewById(R.id.tv_turnover);
            tvStatus   = v.findViewById(R.id.tv_status);
            tvIcon     = v.findViewById(R.id.tv_category_icon);
            avatarBg   = v.findViewById(R.id.view_avatar_bg);
        }

        void bind(BusinessVatStatus b) {
            tvName.setText(b.getBusinessName());
            tvRegNo.setText("BUS-" + String.format("%08X", b.getId())); // Placeholder for reg no
            tvType.setText(b.getBusinessTypeName());
            tvDistrict.setText(b.getDistrictName());
            tvTurnover.setText(formatCurrency(b.getAnnualTurnover()));
            tvStatus.setText(b.getStatus());

            // Icon mapping
            String cat = b.getBusinessCategoryName();
            if (cat != null) {
                if (cat.contains("Manufacturing")) tvIcon.setText("🏭");
                else if (cat.contains("Trading"))  tvIcon.setText("🛍️");
                else if (cat.contains("Service"))  tvIcon.setText("💼");
                else tvIcon.setText("🏢");
            }

            // Status color
            String status = b.getStatus();
            int color;
            if ("Active".equals(status)) {
                color = itemView.getContext().getResources().getColor(R.color.success);
            } else if ("Pending".equals(status)) {
                color = itemView.getContext().getResources().getColor(R.color.warning);
            } else {
                color = itemView.getContext().getResources().getColor(R.color.error);
            }
            tvStatus.getBackground().setTint(color);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(b);
            });
        }

        private String formatCurrency(double amount) {
            if (amount >= 10000000) return String.format(Locale.getDefault(), "৳%.2f Cr", amount / 10000000);
            if (amount >= 100000)   return String.format(Locale.getDefault(), "৳%.2f L", amount / 100000);
            return String.format(Locale.getDefault(), "৳%,.0f", amount);
        }
    }
}
