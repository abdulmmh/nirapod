package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Assessment;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AuditAssessmentTabFragment extends Fragment {

    private static final String ARG_CASE_ID = "case_id";
    private long caseId;
    private ProgressBar pb;
    private View layoutContent;

    public static AuditAssessmentTabFragment newInstance(long caseId) {
        AuditAssessmentTabFragment f = new AuditAssessmentTabFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CASE_ID, caseId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) caseId = getArguments().getLong(ARG_CASE_ID);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_tab_assessment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutContent = view;
        layoutContent.setVisibility(View.GONE);
        loadAssessment();
    }

    private void loadAssessment() {
        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Assessment> resp = RetrofitClient.auditApi(tm).getMyAssessment(caseId).execute();
                requireActivity().runOnUiThread(() -> {
                    if (resp.isSuccessful() && resp.body() != null) {
                        bind(resp.body());
                        layoutContent.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void bind(Assessment a) {
        ((TextView)layoutContent.findViewById(R.id.tv_assessment_no)).setText("Ref: " + a.getAssessmentNo());
        ((TextView)layoutContent.findViewById(R.id.tv_tp_name)).setText(a.getTaxpayerName());
        ((TextView)layoutContent.findViewById(R.id.tv_tin)).setText(a.getTinNumber());
        ((TextView)layoutContent.findViewById(R.id.tv_year)).setText(a.getFiscalYear());

        ((TextView)layoutContent.findViewById(R.id.tv_declared_tax)).setText(format(a.getDeclaredTax()));
        ((TextView)layoutContent.findViewById(R.id.tv_assessed_tax)).setText(format(a.getAssessedTax()));
        ((TextView)layoutContent.findViewById(R.id.tv_additional_tax)).setText(format(a.getAdditionalTax()));
        
        ((TextView)layoutContent.findViewById(R.id.tv_penalty_label)).setText(String.format(Locale.US, "Penalty (%.0f%%)", a.getPenaltyRate()));
        ((TextView)layoutContent.findViewById(R.id.tv_penalty_amount)).setText(format(a.getPenaltyAmount()));
        ((TextView)layoutContent.findViewById(R.id.tv_total_demand)).setText(format(a.getTotalDemand()));

        ((TextView)layoutContent.findViewById(R.id.tv_appeal_rights)).setText(a.getAppealRights());
        ((TextView)layoutContent.findViewById(R.id.tv_deadline)).setText(a.getPaymentDeadline());
        ((TextView)layoutContent.findViewById(R.id.tv_approved_by)).setText(a.getApprovedBy());

        layoutContent.findViewById(R.id.btn_file_appeal).setOnClickListener(v -> {
            FileAppealFragment f = FileAppealFragment.newInstance(a);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, f)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private String format(double amount) {
        return String.format(Locale.US, "%,.2f", amount);
    }
}
