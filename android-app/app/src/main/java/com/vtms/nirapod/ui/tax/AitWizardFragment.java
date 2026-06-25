package com.vtms.nirapod.ui.tax;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AitRecord;
import com.vtms.nirapod.data.model.Taxpayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AitWizardFragment extends Fragment {

    private int currentStep = 1;
    private AitRecord draftRecord = new AitRecord();
    private Taxpayer taxpayer;
    
    private Button btnNext, btnBack;
    private TextView tvStep1Dot, tvStep2Dot, tvStep3Dot;
    private FrameLayout container;
    
    // Step Views
    private View step1View, step2View, step3View;
    
    // Selected Files
    private List<Uri> selectedFileUris = new ArrayList<>();
    private static final int PICK_FILE_REQUEST = 101;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ait_wizard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        btnNext = view.findViewById(R.id.btn_next);
        btnBack = view.findViewById(R.id.btn_back);
        tvStep1Dot = view.findViewById(R.id.tv_step1_dot);
        tvStep2Dot = view.findViewById(R.id.tv_step2_dot);
        tvStep3Dot = view.findViewById(R.id.tv_step3_dot);
        container = view.findViewById(R.id.wizard_container);

        setupStepViews();
        loadTaxpayerProfile();
        updateStepUI();

        btnNext.setOnClickListener(v -> {
            if (currentStep < 3) {
                if (validateCurrentStep()) {
                    currentStep++;
                    updateStepUI();
                }
            } else {
                if (validateCurrentStep()) {
                    submitRecord();
                }
            }
        });

        btnBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                currentStep--;
                updateStepUI();
            }
        });
    }

    private void setupStepViews() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        step1View = inflater.inflate(R.layout.fragment_ait_step1_taxpayer, container, false);
        step2View = inflater.inflate(R.layout.fragment_ait_step2_details, container, false);
        step3View = inflater.inflate(R.layout.fragment_ait_step3_review, container, false);
        
        setupStep2Logic();
        setupStep3Logic();
    }

    private void setupStep2Logic() {
        MaterialButton btnImport = step2View.findViewById(R.id.btnSourceImport);
        MaterialButton btnSupplier = step2View.findViewById(R.id.btnSourceSupplier);
        MaterialButton btnSalary = step2View.findViewById(R.id.btnSourceSalary);
        MaterialButton btnContractor = step2View.findViewById(R.id.btnSourceContractor);
        
        View cardImport = step2View.findViewById(R.id.cardImportDetails);

        View.OnClickListener sourceListener = v -> {
            // Reset colors
            btnImport.setStrokeColorResource(R.color.divider);
            btnSupplier.setStrokeColorResource(R.color.divider);
            btnSalary.setStrokeColorResource(R.color.divider);
            btnContractor.setStrokeColorResource(R.color.divider);
            
            MaterialButton btn = (MaterialButton) v;
            btn.setStrokeColorResource(R.color.primary);
            
            String selected = btn.getText().toString().toUpperCase().replace(" ", "_");
            if (selected.contains("IMPORT")) selected = "IMPORT";
            if (selected.contains("SUPPLIER")) selected = "SUPPLIER";
            if (selected.contains("SALARY")) selected = "SALARY";
            if (selected.contains("CONTRACTOR")) selected = "CONTRACTOR";
            
            draftRecord.setSourceType(selected);
            cardImport.setVisibility(selected.equals("IMPORT") ? View.VISIBLE : View.GONE);
        };

        btnImport.setOnClickListener(sourceListener);
        btnSupplier.setOnClickListener(sourceListener);
        btnSalary.setOnClickListener(sourceListener);
        btnContractor.setOnClickListener(sourceListener);

        EditText etVal = step2View.findViewById(R.id.etTaxableValue);
        EditText etRate = step2View.findViewById(R.id.etAitRate);
        TextView tvCalc = step2View.findViewById(R.id.tvCalculatedAit);

        TextWatcher tw = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateAit(etVal, etRate, tvCalc);
            }
            @Override public void afterTextChanged(Editable s) {}
        };
        etVal.addTextChangedListener(tw);
        etRate.addTextChangedListener(tw);
    }

    private void setupStep3Logic() {
        step3View.findViewById(R.id.btnUpload).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Select Documents"), PICK_FILE_REQUEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    selectedFileUris.add(data.getClipData().getItemAt(i).getUri());
                }
            } else if (data.getData() != null) {
                selectedFileUris.add(data.getData());
            }
            updateFileListUI();
        }
    }

    private void updateFileListUI() {
        TextView tvList = step3View.findViewById(R.id.tvFileList);
        if (selectedFileUris.isEmpty()) {
            tvList.setVisibility(View.GONE);
        } else {
            tvList.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder("Selected Files:\n");
            for (Uri uri : selectedFileUris) {
                sb.append("• ").append(uri.getLastPathSegment()).append("\n");
            }
            tvList.setText(sb.toString());
        }
    }

    private void calculateAit(EditText etVal, EditText etRate, TextView tvCalc) {
        try {
            double val = Double.parseDouble(etVal.getText().toString());
            double rate = Double.parseDouble(etRate.getText().toString());
            double result = (val * rate) / 100.0;
            draftRecord.setTaxableValue(val);
            draftRecord.setAitRate(rate);
            draftRecord.setCalculatedAitAmount(result);
            tvCalc.setText(String.format(Locale.US, "৳%,.2f", result));
        } catch (Exception e) {
            tvCalc.setText("৳0.00");
        }
    }

    private void loadTaxpayerProfile() {
        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Taxpayer> resp = RetrofitClient.taxpayerApi(tm).getMyProfile().execute();
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        if (resp.isSuccessful() && resp.body() != null) {
                            taxpayer = resp.body();
                            ((TextView)step1View.findViewById(R.id.etFullName)).setText(taxpayer.getDisplayName());
                            ((TextView)step1View.findViewById(R.id.etTin)).setText(taxpayer.getTinNumber());
                            ((TextView)step1View.findViewById(R.id.etType)).setText(taxpayer.getTaxpayerType().getTypeName());
                            draftRecord.setTaxpayerId(taxpayer.getId());
                        } else {
                            Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception ignored) {}
        });
    }

    private boolean validateCurrentStep() {
        if (currentStep == 1) {
            if (taxpayer == null) {
                Toast.makeText(requireContext(), "Taxpayer profile not loaded", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (currentStep == 2) {
            if (draftRecord.getSourceType() == null) {
                Toast.makeText(requireContext(), "Please select a source type", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (draftRecord.getCalculatedAitAmount() <= 0) {
                Toast.makeText(requireContext(), "Please enter valid financial amounts", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ("IMPORT".equals(draftRecord.getSourceType())) {
                EditText etId = step2View.findViewById(R.id.etImportId);
                if (etId.getText().toString().isEmpty()) {
                    etId.setError("Required");
                    return false;
                }
                draftRecord.setImportDutyRecordId(Long.parseLong(etId.getText().toString()));
                draftRecord.setHsCode(((EditText)step2View.findViewById(R.id.etHsCode)).getText().toString());
            }
        }
        if (currentStep == 3) {
            if (selectedFileUris.isEmpty()) {
                Toast.makeText(requireContext(), "Please upload at least one supporting document", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void updateStepUI() {
        container.removeAllViews();
        btnBack.setVisibility(currentStep > 1 ? View.VISIBLE : View.GONE);
        
        resetDots();

        if (currentStep == 1) {
            container.addView(step1View);
            setDotActive(tvStep1Dot);
            btnNext.setText("Next: AIT Details");
        } else if (currentStep == 2) {
            container.addView(step2View);
            setDotActive(tvStep2Dot);
            btnNext.setText("Next: Review");
        } else {
            prepareReview();
            container.addView(step3View);
            setDotActive(tvStep3Dot);
            btnNext.setText("Submit AIT Record");
        }
    }

    private void resetDots() {
        tvStep1Dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.divider)));
        tvStep2Dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.divider)));
        tvStep3Dot.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.divider)));
        tvStep1Dot.setTextColor(requireContext().getColor(R.color.text_secondary));
        tvStep2Dot.setTextColor(requireContext().getColor(R.color.text_secondary));
        tvStep3Dot.setTextColor(requireContext().getColor(R.color.text_secondary));
    }

    private void setDotActive(TextView tv) {
        tv.setBackgroundTintList(android.content.res.ColorStateList.valueOf(requireContext().getColor(R.color.primary)));
        tv.setTextColor(requireContext().getColor(R.color.white));
    }

    private void prepareReview() {
        ((TextView)step3View.findViewById(R.id.tvReviewName)).setText(taxpayer.getDisplayName());
        ((TextView)step3View.findViewById(R.id.tvReviewTin)).setText(taxpayer.getTinNumber());
        ((TextView)step3View.findViewById(R.id.tvReviewSource)).setText(draftRecord.getSourceType());
        ((TextView)step3View.findViewById(R.id.tvReviewTaxable)).setText(String.format(Locale.US, "৳%,.2f", draftRecord.getTaxableValue()));
        ((TextView)step3View.findViewById(R.id.tvReviewRate)).setText(String.format(Locale.US, "%.1f%%", draftRecord.getAitRate()));
        ((TextView)step3View.findViewById(R.id.tvReviewAit)).setText(String.format(Locale.US, "৳%,.2f", draftRecord.getCalculatedAitAmount()));
    }

    private void submitRecord() {
        TokenManager tm = new TokenManager(requireContext());
        btnNext.setEnabled(false);
        btnNext.setText("Submitting...");

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<AitRecord> resp = RetrofitClient.aitApi(tm).createAit(draftRecord).execute();
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        if (resp.isSuccessful()) {
                            Toast.makeText(requireContext(), "AIT record created successfully!", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            btnNext.setEnabled(true);
                            btnNext.setText("Submit AIT Record");
                            Toast.makeText(requireContext(), "Submission failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        btnNext.setEnabled(true);
                        btnNext.setText("Submit AIT Record");
                        Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
