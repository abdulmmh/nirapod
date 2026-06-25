package com.vtms.nirapod.ui.payment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Payment;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class PaymentListFragment extends Fragment {

    private PaymentAdapter adapter;
    private ProgressBar pbLoading;
    private TextView tvEmpty;
    private TextView tvKpiPending, tvKpiReview, tvKpiCompleted, tvKpiFailed, tvKpiTotalPaid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbLoading = view.findViewById(R.id.pb_loading);
        tvEmpty   = view.findViewById(R.id.tv_empty);
        
        tvKpiPending   = view.findViewById(R.id.tv_kpi_pending);
        tvKpiReview    = view.findViewById(R.id.tv_kpi_review);
        tvKpiCompleted = view.findViewById(R.id.tv_kpi_completed);
        tvKpiFailed    = view.findViewById(R.id.tv_kpi_failed);
        tvKpiTotalPaid = view.findViewById(R.id.tv_kpi_total_paid);

        RecyclerView rv = view.findViewById(R.id.rv_payments);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PaymentAdapter();
        adapter.setListener(item -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, PaymentDetailFragment.newInstance(item))
                    .addToBackStack(null)
                    .commit();
        });
        rv.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<Payment>> response =
                        RetrofitClient.paymentApi(tm).getMyPayments().execute();

                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pbLoading.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Payment> items = response.body();
                            adapter.setItems(items);
                            updateKpis(items);
                            if (items.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                        } else {
                            tvEmpty.setText("Failed to load payment records");
                            tvEmpty.setVisibility(View.VISIBLE);
                            updateKpis(null);
                        }
                    });
                }
            } catch (Exception e) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pbLoading.setVisibility(View.GONE);
                        tvEmpty.setText("Error: " + e.getMessage());
                        tvEmpty.setVisibility(View.VISIBLE);
                        updateKpis(null);
                    });
                }
            }
        });
    }

    private void updateKpis(List<Payment> items) {
        if (items == null || items.isEmpty()) {
            tvKpiPending.setText("0");
            tvKpiReview.setText("0");
            tvKpiCompleted.setText("0");
            tvKpiFailed.setText("0");
            tvKpiTotalPaid.setText("৳0");
            return;
        }

        long pending   = items.stream().filter(i -> "Pending".equalsIgnoreCase(i.getStatus())).count();
        long review    = items.stream().filter(i -> "Under Review".equalsIgnoreCase(i.getStatus())).count();
        long completed = items.stream().filter(i -> "Completed".equalsIgnoreCase(i.getStatus())).count();
        long failed    = items.stream().filter(i -> "Failed".equalsIgnoreCase(i.getStatus())).count();
        double total   = items.stream().filter(i -> "Completed".equalsIgnoreCase(i.getStatus())).mapToDouble(Payment::getAmount).sum();

        tvKpiPending.setText(String.valueOf(pending));
        tvKpiReview.setText(String.valueOf(review));
        tvKpiCompleted.setText(String.valueOf(completed));
        tvKpiFailed.setText(String.valueOf(failed));
        tvKpiTotalPaid.setText(formatCurrency(total));
    }

    private String formatCurrency(double amount) {
        if (amount >= 10000000) return String.format(Locale.US, "৳%.2f Cr", amount / 10000000);
        if (amount >= 100000)   return String.format(Locale.US, "৳%.2f L", amount / 100000);
        return String.format(Locale.US, "৳%,.0f", amount);
    }
}
