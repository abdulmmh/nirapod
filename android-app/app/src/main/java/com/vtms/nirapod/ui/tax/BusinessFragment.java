package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.BusinessVatStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class BusinessFragment extends Fragment {

    private List<BusinessVatStatus> allItems = new ArrayList<>();
    private BusinessAdapter adapter;
    private TextView tvActive, tvPending, tvSuspended, tvExpiring, tvTurnover;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        setupRecyclerView(view);
        loadData();

        view.findViewById(R.id.fab_add).setOnClickListener(v -> 
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.navHostFragment, new BusinessCreateFragment())
                .addToBackStack(null)
                .commit());
    }

    private void bindViews(View v) {
        tvActive    = v.findViewById(R.id.tv_kpi_active);
        tvPending   = v.findViewById(R.id.tv_kpi_pending);
        tvSuspended = v.findViewById(R.id.tv_kpi_suspended);
        tvExpiring  = v.findViewById(R.id.tv_kpi_expiring);
        tvTurnover  = v.findViewById(R.id.tv_kpi_turnover);
    }

    private void setupRecyclerView(View v) {
        RecyclerView rv = v.findViewById(R.id.rv_businesses);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BusinessAdapter();
        adapter.setListener(item -> {
            BusinessDetailFragment f = BusinessDetailFragment.newInstance(item.getId());
            requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.navHostFragment, f)
                .addToBackStack(null)
                .commit();
        });
        rv.setAdapter(adapter);
    }

    private void loadData() {
        TokenManager tm = new TokenManager(requireContext());
        long taxpayerId = tm.getTaxpayerId();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<BusinessVatStatus>> response =
                        RetrofitClient.businessApi(tm).getMyBusinesses(taxpayerId).execute();

                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        allItems = response.body();
                        updateKpis(allItems);
                        adapter.setItems(allItems);
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void updateKpis(List<BusinessVatStatus> list) {
        long active = list.stream().filter(b -> "Active".equals(b.getStatus())).count();
        long pending = list.stream().filter(b -> "Pending".equals(b.getStatus())).count();
        long suspended = list.stream().filter(b -> "Suspended".equals(b.getStatus())).count();
        double totalTurnover = list.stream()
                .filter(b -> "Active".equals(b.getStatus()))
                .mapToDouble(BusinessVatStatus::getAnnualTurnover).sum();

        tvActive.setText(String.valueOf(active));
        tvPending.setText(String.valueOf(pending));
        tvSuspended.setText(String.valueOf(suspended));
        tvExpiring.setText("0"); // Placeholder
        tvTurnover.setText(formatCurrency(totalTurnover));
    }

    private String formatCurrency(double amount) {
        if (amount >= 10000000) return String.format(Locale.getDefault(), "৳%.2f Cr", amount / 10000000);
        if (amount >= 100000)   return String.format(Locale.getDefault(), "৳%.2f L", amount / 100000);
        return String.format(Locale.getDefault(), "৳%,.0f", amount);
    }
}
