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
import com.vtms.nirapod.data.model.AuditCase;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AuditListFragment extends Fragment {

    private AuditAdapter adapter;
    private ProgressBar pbLoading;
    private TextView tvEmpty;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbLoading = view.findViewById(R.id.pb_loading);
        tvEmpty   = view.findViewById(R.id.tv_empty);
        RecyclerView rv = view.findViewById(R.id.rv_audits);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AuditAdapter();
        adapter.setListener(item -> {
            AuditDetailFragment detail = AuditDetailFragment.newInstance(item.getId());
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, detail)
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
                Response<List<AuditCase>> response =
                        RetrofitClient.auditApi(tm).getMyAudits().execute();

                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        List<AuditCase> items = response.body();
                        adapter.setItems(items);
                        if (items.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setText("Failed to load audit cases");
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    tvEmpty.setText("Error: " + e.getMessage());
                    tvEmpty.setVisibility(View.VISIBLE);
                });
            }
        });
    }
}
