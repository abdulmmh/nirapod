package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AuditCase;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AuditDetailFragment extends Fragment {

    private static final String ARG_CASE_ID = "case_id";
    private long caseId;
    private AuditCase auditCase;
    private ProgressBar pbLoading;
    private TextView tvHeaderCaseNo, tvHeaderType, tvHeaderYear, tvHeaderStatus;
    private Button btnDetails, btnQueries, btnDocs, btnAssessment;

    public static AuditDetailFragment newInstance(long id) {
        AuditDetailFragment f = new AuditDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CASE_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            caseId = getArguments().getLong(ARG_CASE_ID);
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bindViews(view);
        loadData();

        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().onBackPressed());
        
        btnDetails.setOnClickListener(v -> selectTab("details"));
        btnQueries.setOnClickListener(v -> selectTab("queries"));
        btnDocs.setOnClickListener(v -> selectTab("docs"));
        btnAssessment.setOnClickListener(v -> selectTab("assessment"));
    }

    private void bindViews(View v) {
        pbLoading = v.findViewById(R.id.pb_loading);
        tvHeaderCaseNo = v.findViewById(R.id.tv_header_case_no);
        tvHeaderType   = v.findViewById(R.id.tv_header_type);
        tvHeaderYear   = v.findViewById(R.id.tv_header_year);
        tvHeaderStatus = v.findViewById(R.id.tv_header_status);

        btnDetails    = v.findViewById(R.id.btn_tab_details);
        btnQueries    = v.findViewById(R.id.btn_tab_queries);
        btnDocs       = v.findViewById(R.id.btn_tab_docs);
        btnAssessment = v.findViewById(R.id.btn_tab_assessment);
    }

    private void loadData() {
        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<AuditCase> response = RetrofitClient.auditApi(tm).getMyAuditById(caseId).execute();
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        auditCase = response.body();
                        updateHeader();
                        selectTab("details");
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void updateHeader() {
        tvHeaderCaseNo.setText(auditCase.getCaseNo());
        tvHeaderType.setText(auditCase.getAuditType());
        tvHeaderYear.setText(auditCase.getFiscalYear());
        tvHeaderStatus.setText(auditCase.getStatus().replace("_", " "));
        
        // Color status
        int color = auditCase.getStatus().contains("APPROVED") ? R.color.success : R.color.primary;
        tvHeaderStatus.getBackground().setTint(requireContext().getColor(color));

        btnAssessment.setVisibility(auditCase.isHasAssessment() ? View.VISIBLE : View.GONE);
    }

    private void selectTab(String tab) {
        resetTabButtons();
        Fragment f;
        if (tab.equals("queries")) {
            btnQueries.setBackgroundColor(requireContext().getColor(R.color.primary));
            btnQueries.setTextColor(requireContext().getColor(R.color.white));
            f = AuditQueriesTabFragment.newInstance(caseId);
        } else if (tab.equals("docs")) {
            btnDocs.setBackgroundColor(requireContext().getColor(R.color.primary));
            btnDocs.setTextColor(requireContext().getColor(R.color.white));
            f = AuditDocsTabFragment.newInstance(caseId);
        } else if (tab.equals("assessment")) {
            btnAssessment.setBackgroundColor(requireContext().getColor(R.color.primary));
            btnAssessment.setTextColor(requireContext().getColor(R.color.white));
            f = AuditAssessmentTabFragment.newInstance(caseId);
        } else {
            btnDetails.setBackgroundColor(requireContext().getColor(R.color.primary));
            btnDetails.setTextColor(requireContext().getColor(R.color.white));
            f = AuditOverviewTabFragment.newInstance(auditCase);
        }

        getChildFragmentManager().beginTransaction()
                .replace(R.id.tab_container, f)
                .commit();
    }

    private void resetTabButtons() {
        Button[] btns = {btnDetails, btnQueries, btnDocs, btnAssessment};
        for (Button b : btns) {
            b.setBackgroundColor(requireContext().getColor(R.color.white));
            b.setTextColor(requireContext().getColor(R.color.text_primary));
        }
    }
}
