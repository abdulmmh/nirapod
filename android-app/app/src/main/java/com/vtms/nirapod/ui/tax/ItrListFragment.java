package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.ItrRecord;
import java.util.List;
import java.util.stream.Collectors;

public class ItrListFragment extends Fragment {

    private ItrViewModel viewModel;
    private ItrAdapter   adapter;
    private TextView tvKpiTotal, tvKpiAccepted, tvKpiPending, tvKpiOverdue;
    private List<ItrRecord> allItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itr_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ProgressBar  progressBar  = view.findViewById(R.id.progressBar);
        TextView     tvEmpty      = view.findViewById(R.id.tvEmpty);
        
        tvKpiTotal = view.findViewById(R.id.tv_kpi_total);
        tvKpiAccepted = view.findViewById(R.id.tv_kpi_accepted);
        tvKpiPending = view.findViewById(R.id.tv_kpi_pending);
        tvKpiOverdue = view.findViewById(R.id.tv_kpi_overdue);

        adapter = new ItrAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        adapter.setListener(item -> {
            ItrDetailFragment detail = ItrDetailFragment.newInstance(item);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment, detail)
                    .addToBackStack(null)
                    .commit();
        });

        ChipGroup chipGroup = view.findViewById(R.id.chipGroupStatus);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (allItems == null) return;
            int checkedId = checkedIds.isEmpty() ? R.id.chipAll : checkedIds.get(0);
            filterByStatus(checkedId);
        });

        viewModel = new ViewModelProvider(this).get(ItrViewModel.class);

        viewModel.getItrResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isLoading()) {
                progressBar.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
            } else if (result.isSuccess()) {
                progressBar.setVisibility(View.GONE);
                if (result.data == null || result.data.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    updateKpis(null);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    allItems = result.data;
                    adapter.setItems(result.data);
                    updateKpis(result.data);
                }
            } else {
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText(result.message);
                tvEmpty.setVisibility(View.VISIBLE);
                updateKpis(null);
            }
        });

        viewModel.loadMyReturns();
    }

    private void filterByStatus(int chipId) {
        String targetStatus = null;
        if (chipId == R.id.chipSubmitted) targetStatus = "Submitted";
        else if (chipId == R.id.chipUnderReview) targetStatus = "Under Review";
        else if (chipId == R.id.chipAccepted) targetStatus = "Accepted";
        else if (chipId == R.id.chipDraft) targetStatus = "Draft";
        else if (chipId == R.id.chipOverdue) targetStatus = "Overdue";

        if (targetStatus == null) {
            adapter.setItems(allItems);
        } else {
            String finalTarget = targetStatus;
            List<ItrRecord> filtered = allItems.stream()
                    .filter(i -> finalTarget.equalsIgnoreCase(i.getStatus()))
                    .collect(Collectors.toList());
            adapter.setItems(filtered);
        }
    }

    private void updateKpis(List<ItrRecord> items) {
        if (items == null || items.isEmpty()) {
            tvKpiTotal.setText("0");
            tvKpiAccepted.setText("0");
            tvKpiPending.setText("0");
            tvKpiOverdue.setText("0");
            return;
        }

        long total = items.size();
        long accepted = items.stream().filter(i -> "Accepted".equalsIgnoreCase(i.getStatus())).count();
        long pending = items.stream().filter(i -> "Submitted".equalsIgnoreCase(i.getStatus()) || "Under Review".equalsIgnoreCase(i.getStatus())).count();
        long overdue = items.stream().filter(i -> "Overdue".equalsIgnoreCase(i.getStatus())).count();

        tvKpiTotal.setText(String.valueOf(total));
        tvKpiAccepted.setText(String.valueOf(accepted));
        tvKpiPending.setText(String.valueOf(pending));
        tvKpiOverdue.setText(String.valueOf(overdue));
    }
}
