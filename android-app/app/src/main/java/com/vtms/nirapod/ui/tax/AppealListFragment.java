package com.vtms.nirapod.ui.tax;

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
import com.vtms.nirapod.data.model.Appeal;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AppealListFragment extends Fragment {

    private RecyclerView rvAppeals;
    private ProgressBar pbLoading;
    private View llEmpty;
    private TextView tvEmpty;
    private AppealAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appeal_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAppeals = view.findViewById(R.id.rvAppeals);
        pbLoading = view.findViewById(R.id.pbLoading);
        llEmpty = view.findViewById(R.id.llEmpty);
        tvEmpty = view.findViewById(R.id.tvEmpty);

        adapter = new AppealAdapter();
        rvAppeals.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAppeals.setAdapter(adapter);

        adapter.setListener(appeal -> {
            if (isAdded() && getActivity() != null) {
                AppealDetailFragment detail = AppealDetailFragment.newInstance(appeal.getId());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.navHostFragment, detail)
                        .addToBackStack(null)
                        .commit();
            }
        });

        loadAppeals();
    }

    private void loadAppeals() {
        if (!isAdded() || getContext() == null) return;

        pbLoading.setVisibility(View.VISIBLE);
        llEmpty.setVisibility(View.GONE);

        TokenManager tm = new TokenManager(getContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<Appeal>> response = RetrofitClient.appealApi(tm).getMyAppeals().execute();
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        pbLoading.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            adapter.setItems(response.body());
                            llEmpty.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setText("No appeals found");
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    });
                }
            } catch (Exception e) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        pbLoading.setVisibility(View.GONE);
                        tvEmpty.setText("Network error");
                        llEmpty.setVisibility(View.VISIBLE);
                    });
                }
            }
        });
    }
}
