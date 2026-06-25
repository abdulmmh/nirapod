package com.vtms.nirapod.ui.payment;

import android.graphics.Color;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.OutstandingItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OutstandingAdapter
        extends RecyclerView.Adapter<OutstandingAdapter.ViewHolder> {

    public interface OnSelectListener {
        void onSelect(OutstandingItem item);
        void onUnselect();
    }

    private final List<OutstandingItem> allItems = new ArrayList<>();
    private final List<OutstandingItem> displayedItems = new ArrayList<>();
    private final OnSelectListener listener;
    private OutstandingItem selectedItem = null;

    public OutstandingAdapter(OnSelectListener listener) {
        this.listener = listener;
    }

    public void setItems(List<OutstandingItem> data) {
        allItems.clear();
        allItems.addAll(data);
        resetFilter();
    }

    public void resetFilter() {
        selectedItem = null;
        displayedItems.clear();
        displayedItems.addAll(allItems);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        resetFilter();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_outstanding, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        OutstandingItem item = displayedItems.get(position);
        boolean isSelected = (selectedItem != null && selectedItem.getReturnNo().equals(item.getReturnNo()));

        h.tvLabel.setText(item.getLabel());
        h.tvRefNo.setText(item.getReturnNo());
        h.tvDueDate.setText(String.format("· Due %s", item.getDueDate()));
        h.tvAmount.setText(formatAmount(item.getOutstanding()));

        // Type badge
        h.tvTypeBadge.setText(item.getType());
        applyTypeBadge(h, item.getType());

        // Overdue badge
        if (item.isOverdue()) {
            h.tvOverdue.setVisibility(View.VISIBLE);
            h.tvDueDate.setTextColor(Color.parseColor("#C62828"));
        } else {
            h.tvOverdue.setVisibility(View.GONE);
            h.tvDueDate.setTextColor(Color.parseColor("#607D8B"));
        }

        // Selected state UI
        h.tvSelectBtn.setText(isSelected ? "✕ Unselect" : "Select");
        h.tvSelectBtn.setTextColor(isSelected
                ? Color.parseColor("#C62828") 
                : Color.parseColor("#607D8B"));
        
        h.card.setCardBackgroundColor(isSelected
                ? Color.parseColor("#F1F8F4")
                : Color.WHITE);

        // Left border
        if (isSelected) {
            h.leftBorder.setBackgroundColor(Color.parseColor("#2E7D5E"));
        } else if (item.isOverdue()) {
            h.leftBorder.setBackgroundColor(Color.parseColor("#C62828"));
        } else {
            h.leftBorder.setBackgroundColor(Color.TRANSPARENT);
        }

        h.tvSelectBtn.setOnClickListener(v -> {
            if (isSelected) {
                resetFilter();
                listener.onUnselect();
            } else {
                selectedItem = item;
                displayedItems.clear();
                displayedItems.add(item);
                notifyDataSetChanged();
                listener.onSelect(item);
            }
        });
    }

    @Override
    public int getItemCount() { return displayedItems.size(); }

    private void applyTypeBadge(ViewHolder h, String type) {
        int bg, fg;
        switch (type) {
            case "Income Tax":
                bg = Color.parseColor("#E3F2FD"); fg = Color.parseColor("#1565C0"); break;
            case "VAT":
                bg = Color.parseColor("#E8F5E9"); fg = Color.parseColor("#2E7D32"); break;
            case "AIT":
                bg = Color.parseColor("#FFF3E0"); fg = Color.parseColor("#E65100"); break;
            case "Penalty":
                bg = Color.parseColor("#FFEBEE"); fg = Color.parseColor("#C62828"); break;
            case "Demand Notice":
                bg = Color.parseColor("#FFF8E1"); fg = Color.parseColor("#F57F17"); break;
            default:
                bg = Color.parseColor("#F5F5F5"); fg = Color.parseColor("#546E7A"); break;
        }
        h.tvTypeBadge.setBackgroundColor(bg);
        h.tvTypeBadge.setTextColor(fg);
    }

    private String formatAmount(double amount) {
        if (amount >= 100000) {
            return "৳" + String.format(Locale.US, "%.2f", amount / 100000) + "L";
        }
        return "৳" + String.format(Locale.US, "%,.0f", amount);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        View leftBorder;
        TextView tvTypeBadge, tvAmount, tvLabel, tvRefNo, tvDueDate, tvOverdue, tvSelectBtn;

        ViewHolder(View v) {
            super(v);
            card        = v.findViewById(R.id.cardOutstanding);
            leftBorder  = v.findViewById(R.id.viewLeftBorder);
            tvTypeBadge = v.findViewById(R.id.tvTypeBadge);
            tvAmount    = v.findViewById(R.id.tvOutstandingAmount);
            tvLabel     = v.findViewById(R.id.tvOutstandingLabel);
            tvRefNo     = v.findViewById(R.id.tvOutstandingRef);
            tvDueDate   = v.findViewById(R.id.tvOutstandingDue);
            tvOverdue   = v.findViewById(R.id.tvOverdueBadge);
            tvSelectBtn = v.findViewById(R.id.tvSelectBtn);
        }
    }
}
