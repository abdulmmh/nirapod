package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Appeal;
import com.vtms.nirapod.data.model.Assessment;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class FileAppealFragment extends Fragment {

    private static final String ARG_ASSESSMENT = "assessment";
    private Assessment assessment;
    private AutoCompleteTextView actvAppealType;
    private EditText etDisputedAmount, etGrounds, etRelief, etRemarks, etEvidence;
    private TextView tvCharCount;
    private ProgressBar pb;
    private Button btnSubmit;

    public static FileAppealFragment newInstance(Assessment a) {
        FileAppealFragment f = new FileAppealFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ASSESSMENT, a);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assessment = (Assessment) getArguments().getSerializable(ARG_ASSESSMENT);
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_file_appeal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bindViews(view);
        setupAppealTypes();
        setupCharacterCounter();

        ((TextView)view.findViewById(R.id.tv_filing_info)).setText(
                String.format("Filing appeal for: Case %s\n· Assessment %s", 
                        assessment.getAssessmentNo(), // Assuming case no might be related or we use assessment no
                        assessment.getAssessmentNo()));
        
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        btnSubmit.setOnClickListener(v -> submit());
    }

    private void bindViews(View v) {
        actvAppealType    = v.findViewById(R.id.actv_appeal_type);
        etDisputedAmount  = v.findViewById(R.id.et_disputed_amount);
        etGrounds         = v.findViewById(R.id.et_grounds);
        etRelief          = v.findViewById(R.id.et_relief);
        etRemarks         = v.findViewById(R.id.et_remarks);
        etEvidence        = v.findViewById(R.id.et_evidence);
        tvCharCount       = v.findViewById(R.id.tv_char_count);
        pb                = v.findViewById(R.id.pb_submitting);
        btnSubmit         = v.findViewById(R.id.btn_submit);
    }

    private void setupAppealTypes() {
        String[] types = {"Against Demand Notice", "Against Assessment Order", "Penalty Appeal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, types);
        actvAppealType.setAdapter(adapter);
    }

    private void setupCharacterCounter() {
        etGrounds.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                tvCharCount.setText(len + " characters");
                if (len < 50) {
                    tvCharCount.setTextColor(requireContext().getColor(R.color.error));
                } else {
                    tvCharCount.setTextColor(requireContext().getColor(R.color.success));
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void submit() {
        String type = actvAppealType.getText().toString();
        String grounds = etGrounds.getText().toString().trim();
        String relief = etRelief.getText().toString().trim();
        String disputedStr = etDisputedAmount.getText().toString().trim();
        String remarks = etRemarks.getText().toString().trim();
        String evidence = etEvidence.getText().toString().trim();

        if (type.isEmpty()) {
            Toast.makeText(requireContext(), "Please select appeal type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (grounds.length() < 50) {
            etGrounds.setError("Minimum 50 characters required");
            return;
        }
        if (disputedStr.isEmpty()) {
            etDisputedAmount.setError("Required");
            return;
        }

        double disputed = Double.parseDouble(disputedStr);

        Appeal req = new Appeal();
        req.setAuditCaseId(assessment.getAuditCaseId());
        req.setAssessmentId(assessment.getId());
        req.setAppealType(type.toUpperCase().replace(" ", "_"));
        req.setGroundsText(grounds);
        req.setReliefSought(relief);
        req.setSupportingEvidence(evidence);
        req.setRemarks(remarks);
        req.setDisputedAmount(disputed);
        req.setDemandedAmount(assessment.getTotalDemand());

        TokenManager tm = new TokenManager(requireContext());
        btnSubmit.setEnabled(false);
        pb.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Appeal> resp = RetrofitClient.appealApi(tm).fileAppeal(req).execute();
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    btnSubmit.setEnabled(true);
                    if (resp.isSuccessful()) {
                        Toast.makeText(requireContext(), "Appeal filed successfully!", Toast.LENGTH_LONG).show();
                        requireActivity().getOnBackPressedDispatcher().onBackPressed();
                    } else {
                        Toast.makeText(requireContext(), "Failed to file appeal", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    btnSubmit.setEnabled(true);
                    Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
