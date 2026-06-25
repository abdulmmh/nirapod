package com.vtms.nirapod.ui.tax;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.ItrRecord;
import java.util.*;
import java.util.Locale;

public class ItrAdapter extends RecyclerView.Adapter<ItrAdapter.ViewHolder> {

    private List<ItrRecord> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItrRecord item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ItrRecord> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itr, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        ItrRecord item = items.get(position);
        h.tvReturnNo.setText(item.getReturnNo());
        h.tvYears.setText(String.format("Assessment Year: %s - Income Year: %s", item.getAssessmentYear(), item.getIncomeYear()));
        h.tvStatus.setText(item.getStatus());
        
        h.tvGrossIncome.setText(format(item.getGrossIncome()));
        h.tvTaxableIncome.setText(format(item.getTaxableIncome()));
        h.tvNetTax.setText(format(item.getNetTaxPayable()));
        h.tvRefundable.setText(item.getRefundable() > 0 ? format(item.getRefundable()) : "—");
        
        h.tvFiledDate.setText("Filed: " + item.getSubmissionDate());
        h.tvDueDate.setText("Due: " + item.getDueDate());

        // Overdue logic
        boolean isOverdue = "Overdue".equalsIgnoreCase(item.getStatus());
        h.layoutOverdue.setVisibility(isOverdue ? View.VISIBLE : View.GONE);
        if (isOverdue) {
            h.tvOverdueMsg.setText("Due date was " + item.getDueDate() + " — return is overdue");
        }

        // Color status
        int color;
        String s = item.getStatus();
        if ("Accepted".equalsIgnoreCase(s)) {
            color = h.itemView.getContext().getColor(R.color.success);
        } else if ("Overdue".equalsIgnoreCase(s) || "Rejected".equalsIgnoreCase(s)) {
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
        if (amount >= 10000000) return String.format(Locale.US, "৳%.2f Cr", amount / 10000000);
        if (amount >= 100000)   return String.format(Locale.US, "৳%.2f L", amount / 100000);
        return String.format(Locale.US, "৳%,.0f", amount);
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReturnNo, tvYears, tvStatus;
        TextView tvGrossIncome, tvTaxableIncome, tvNetTax, tvRefundable;
        TextView tvFiledDate, tvDueDate, tvOverdueMsg;
        View layoutOverdue, btnView;

        ViewHolder(View v) {
            super(v);
            tvReturnNo = v.findViewById(R.id.tv_return_no);
            tvYears    = v.findViewById(R.id.tv_years);
            tvStatus   = v.findViewById(R.id.tv_status);
            tvGrossIncome = v.findViewById(R.id.tv_gross_income);
            tvTaxableIncome = v.findViewById(R.id.tv_taxable_income);
            tvNetTax = v.findViewById(R.id.tv_net_tax);
            tvRefundable = v.findViewById(R.id.tv_refundable);
            tvFiledDate = v.findViewById(R.id.tv_filed_date);
            tvDueDate = v.findViewById(R.id.tv_due_date);
            tvOverdueMsg = v.findViewById(R.id.tv_overdue_msg);
            layoutOverdue = v.findViewById(R.id.layout_overdue);
            btnView = v.findViewById(R.id.btn_view);
        }
    }
}
