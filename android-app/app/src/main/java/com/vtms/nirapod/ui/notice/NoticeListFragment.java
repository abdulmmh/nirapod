package com.vtms.nirapod.ui.notice;

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
import com.google.android.material.chip.ChipGroup;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Notice;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import retrofit2.Response;

public class NoticeListFragment extends Fragment {

    private NoticeAdapter adapter;
    private TokenManager  tm;
    private List<Notice>  allNotices;
    
    private TextView tvKpiUnread, tvKpiResponded, tvKpiUrgent, tvKpiExpired, tvUnreadBadge;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tm = new TokenManager(requireContext());

        bindViews(view);
        setupRecyclerView(view);
        setupFilters(view);

        loadNotices(view.findViewById(R.id.progressBar), view.findViewById(R.id.tvEmpty));
    }

    private void bindViews(View v) {
        tvKpiUnread    = v.findViewById(R.id.tv_kpi_unread);
        tvKpiResponded = v.findViewById(R.id.tv_kpi_responded);
        tvKpiUrgent    = v.findViewById(R.id.tv_kpi_urgent);
        tvKpiExpired   = v.findViewById(R.id.tv_kpi_expired);
        tvUnreadBadge  = v.findViewById(R.id.tvUnreadBadge);
    }

    private void setupRecyclerView(View v) {
        RecyclerView rv = v.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NoticeAdapter();
        rv.setAdapter(adapter);

        adapter.setListener(notice -> {
            NoticeDetailFragment detail = NoticeDetailFragment.newInstance(notice);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment, detail)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupFilters(View v) {
        ChipGroup chipGroup = v.findViewById(R.id.chipGroupStatus);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (allNotices == null) return;
            int checkedId = checkedIds.isEmpty() ? R.id.chipAll : checkedIds.get(0);
            applyFilter(checkedId);
        });
    }

    private void loadNotices(ProgressBar pb, TextView tvEmpty) {
        pb.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<Notice>> response =
                        RetrofitClient.noticeApi(tm).getMyNotices().execute();

                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        allNotices = response.body();
                        updateKpis(allNotices);
                        adapter.setItems(allNotices);
                        if (allNotices.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    tvEmpty.setText("Cannot connect to server");
                    tvEmpty.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void updateKpis(List<Notice> notices) {
        long unread    = notices.stream().filter(n -> !n.isRead()).count();
        long responded = notices.stream().filter(n -> "RESPONDED".equalsIgnoreCase(n.getStatus())).count();
        long urgent    = notices.stream().filter(n -> "HIGH".equalsIgnoreCase(n.getPriority())).count();
        long expired   = notices.stream().filter(n -> "EXPIRED".equalsIgnoreCase(n.getStatus())).count();

        tvKpiUnread.setText(String.valueOf(unread));
        tvKpiResponded.setText(String.valueOf(responded));
        tvKpiUrgent.setText(String.valueOf(urgent));
        tvKpiExpired.setText(String.valueOf(expired));

        if (unread > 0) {
            tvUnreadBadge.setText(unread + " Unread");
            tvUnreadBadge.setVisibility(View.VISIBLE);
        } else {
            tvUnreadBadge.setVisibility(View.GONE);
        }
    }

    private void applyFilter(int chipId) {
        if (allNotices == null) return;
        List<Notice> filtered;
        if (chipId == R.id.chipUnread) {
            filtered = allNotices.stream().filter(n -> !n.isRead()).collect(Collectors.toList());
        } else if (chipId == R.id.chipUrgent) {
            filtered = allNotices.stream().filter(n -> "HIGH".equalsIgnoreCase(n.getPriority())).collect(Collectors.toList());
        } else if (chipId == R.id.chipResponded) {
            filtered = allNotices.stream().filter(n -> "RESPONDED".equalsIgnoreCase(n.getStatus())).collect(Collectors.toList());
        } else {
            filtered = allNotices;
        }
        adapter.setItems(filtered);
    }
}
