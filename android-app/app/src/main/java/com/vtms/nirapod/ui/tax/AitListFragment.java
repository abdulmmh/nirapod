package com.vtms.nirapod.ui.tax;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AitRecord;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AitListFragment extends Fragment {

    private AitAdapter adapter;
    private ProgressBar pbLoading;
    private TextView tvEmpty;
    private TextView tvKpiTotal, tvKpiApproved, tvKpiPending, tvKpiCredited;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ait_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbLoading = view.findViewById(R.id.pb_loading);
        tvEmpty   = view.findViewById(R.id.tv_empty);
        tvKpiTotal = view.findViewById(R.id.tv_kpi_total);
        tvKpiApproved = view.findViewById(R.id.tv_kpi_approved);
        tvKpiPending = view.findViewById(R.id.tv_kpi_pending);
        tvKpiCredited = view.findViewById(R.id.tv_kpi_credited);
        
        RecyclerView rv = view.findViewById(R.id.rv_ait);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AitAdapter();
        adapter.setListener(item -> {
            AitDetailFragment detail = AitDetailFragment.newInstance(item.getId());
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, detail)
                    .addToBackStack(null)
                    .commit();
        });
        rv.setAdapter(adapter);

        view.findViewById(R.id.fab_add).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, new AitWizardFragment())
                    .addToBackStack(null)
                    .commit();
        });

        loadData();
    }

    private void loadData() {
        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<AitRecord>> response =
                        RetrofitClient.aitApi(tm).getMyAitRecords().execute();

                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pbLoading.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            List<AitRecord> items = response.body();
                            adapter.setItems(items);
                            updateKpis(items);
                            if (items.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                        } else {
                            tvEmpty.setText("Failed to load AIT records");
                            tvEmpty.setVisibility(View.VISIBLE);
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
                    });
                }
            }
        });
    }

    private void updateKpis(List<AitRecord> items) {
        long total = items.size();
        long approved = items.stream().filter(i -> i.getStatus().equals("APPROVED")).count();
        long pending = items.stream().filter(i -> i.getStatus().equals("PENDING") || i.getStatus().equals("SUBMITTED")).count();
        long credited = items.stream().filter(i -> i.getStatus().equals("CREDITED")).count();

        tvKpiTotal.setText(String.valueOf(total));
        tvKpiApproved.setText(String.valueOf(approved));
        tvKpiPending.setText(String.valueOf(pending));
        tvKpiCredited.setText(String.valueOf(credited));
    }
}
