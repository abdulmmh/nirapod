package com.vtms.nirapod.ui.payment;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.Payment;
import java.util.*;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<Payment> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Payment item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Payment> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int p) {
        Payment item = items.get(p);
        
        String name = item.getTaxpayerName();
        h.tvAvatar.setText(name != null && !name.isEmpty() ? String.valueOf(name.charAt(0)) : "P");
        h.tvTaxpayerName.setText(name);
        h.tvTxnId.setText(item.getTransactionId());
        h.tvStatus.setText(item.getStatus());
        
        h.tvTin.setText(item.getTinNumber());
        h.tvType.setText(item.getPaymentType());
        h.tvAmount.setText(format(item.getAmount()));
        h.tvMethod.setText(item.getPaymentMethod());
        h.tvBank.setText(item.getBankName());
        h.tvReference.setText(item.getReferenceNo() != null ? item.getReferenceNo() : "—");
        h.tvDate.setText(item.getPaymentDate());

        // Color status
        int color;
        String s = item.getStatus();
        if ("Completed".equalsIgnoreCase(s)) {
            color = h.itemView.getContext().getColor(R.color.success);
            h.tvStatus.setTextColor(h.itemView.getContext().getColor(R.color.white));
            h.tvStatus.getBackground().setTint(color);
        } else if ("Failed".equalsIgnoreCase(s)) {
            color = h.itemView.getContext().getColor(R.color.error);
            h.tvStatus.setTextColor(h.itemView.getContext().getColor(R.color.white));
            h.tvStatus.getBackground().setTint(color);
        } else {
            color = h.itemView.getContext().getColor(R.color.primary);
            h.tvStatus.getBackground().setTintList(null); // Reset to default style if needed
        }

        h.btnView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    private String format(double amount) {
        if (amount >= 10000000) return String.format(Locale.US, "৳%.2f Cr", amount / 10000000);
        if (amount >= 100000)   return String.format(Locale.US, "৳%.2f L", amount / 100000);
        return String.format(Locale.US, "৳%,.0f", amount);
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAvatar, tvTaxpayerName, tvTxnId, tvStatus;
        TextView tvTin, tvType, tvAmount, tvMethod, tvBank, tvReference, tvDate;
        View btnView;

        ViewHolder(View v) {
            super(v);
            tvAvatar = v.findViewById(R.id.tv_avatar);
            tvTaxpayerName = v.findViewById(R.id.tv_taxpayer_name);
            tvTxnId = v.findViewById(R.id.tv_txn_id);
            tvStatus = v.findViewById(R.id.tv_status);
            
            tvTin = v.findViewById(R.id.tv_tin);
            tvType = v.findViewById(R.id.tv_type);
            tvAmount = v.findViewById(R.id.tv_amount);
            tvMethod = v.findViewById(R.id.tv_method);
            tvBank = v.findViewById(R.id.tv_bank);
            tvReference = v.findViewById(R.id.tv_reference);
            tvDate = v.findViewById(R.id.tv_date);
            btnView = v.findViewById(R.id.btn_view);
        }
    }
}
