package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Appeal;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AppealDetailFragment extends Fragment {

    private static final String ARG_APPEAL_ID = "appeal_id";
    private long appealId;
    private Appeal appeal;

    public static AppealDetailFragment newInstance(long id) {
        AppealDetailFragment f = new AppealDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_APPEAL_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appealId = getArguments().getLong(ARG_APPEAL_ID);
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appeal_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        loadAppeal(view);
    }

    private void loadAppeal(View root) {
        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Appeal> resp = RetrofitClient.appealApi(tm).getAppealById(appealId).execute();
                requireActivity().runOnUiThread(() -> {
                    if (resp.isSuccessful() && resp.body() != null) {
                        appeal = resp.body();
                        bind(root);
                    } else {
                        Toast.makeText(requireContext(), "Failed to load appeal", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void bind(View root) {
        ((TextView)root.findViewById(R.id.tv_appeal_no)).setText(appeal.getAppealNo());
        ((TextView)root.findViewById(R.id.tv_type)).setText(appeal.getAppealType().replace("_", " "));
        ((TextView)root.findViewById(R.id.tv_filed_at)).setText("Filed " + (appeal.getFiledAt() != null ? appeal.getFiledAt().substring(0, 10) : "---"));
        ((TextView)root.findViewById(R.id.tv_status)).setText(appeal.getStatus());
        
        // Financials
        ((TextView)root.findViewById(R.id.tv_demanded)).setText(String.format(Locale.US, "%,.2f BDT", appeal.getDemandedAmount()));
        ((TextView)root.findViewById(R.id.tv_disputed)).setText(String.format(Locale.US, "%,.2f BDT", appeal.getDisputedAmount()));
        
        // Grounds
        ((TextView)root.findViewById(R.id.tv_grounds)).setText(appeal.getGroundsText());
        ((TextView)root.findViewById(R.id.tv_relief)).setText("Relief Sought: " + (appeal.getReliefSought() != null ? appeal.getReliefSought() : "---"));
        
        // Status color
        int color = R.color.primary;
        if ("DECIDED".equals(appeal.getStatus())) color = R.color.success;
        if ("WITHDRAWN".equals(appeal.getStatus())) color = R.color.text_secondary;
        ((TextView)root.findViewById(R.id.tv_status)).setTextColor(requireContext().getColor(color));
    }
}
