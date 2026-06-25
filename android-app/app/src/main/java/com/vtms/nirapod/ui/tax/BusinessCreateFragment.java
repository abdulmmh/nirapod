package com.vtms.nirapod.ui.tax;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Business;
import com.vtms.nirapod.data.model.Taxpayer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class BusinessCreateFragment extends Fragment {

    private EditText etName, etTin, etOwner, etTradeLicense, etIncorporation, etRegistration, etExpiry, etEmail, etPhone, etAddress, etTurnover, etEmployees, etRemarks;
    private AutoCompleteTextView etType, etCategory, etDivision, etDistrict;
    private TextView tvTpName, tvTpTin;
    private View pbLoading;
    private Taxpayer profile;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bindViews(view);
        setupDropdowns();
        setupDatePickers();
        loadProfile();

        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().onBackPressed());
        view.findViewById(R.id.btn_submit).setOnClickListener(v -> submit());
    }

    private void bindViews(View v) {
        pbLoading = v.findViewById(R.id.pb_loading);
        tvTpName  = v.findViewById(R.id.tv_taxpayer_name);
        tvTpTin   = v.findViewById(R.id.tv_taxpayer_tin);

        etName          = v.findViewById(R.id.et_business_name);
        etTin           = v.findViewById(R.id.et_tin);
        etOwner         = v.findViewById(R.id.et_owner);
        etTradeLicense  = v.findViewById(R.id.et_trade_license);
        etIncorporation = v.findViewById(R.id.et_incorporation_date);
        etRegistration  = v.findViewById(R.id.et_registration_date);
        etExpiry        = v.findViewById(R.id.et_expiry_date);
        etEmail         = v.findViewById(R.id.et_email);
        etPhone         = v.findViewById(R.id.et_phone);
        etAddress       = v.findViewById(R.id.et_address);
        etTurnover      = v.findViewById(R.id.et_turnover);
        etEmployees     = v.findViewById(R.id.et_employees);
        etRemarks       = v.findViewById(R.id.et_remarks);

        etType     = v.findViewById(R.id.spinner_type);
        etCategory = v.findViewById(R.id.spinner_category);
        etDivision = v.findViewById(R.id.spinner_division);
        etDistrict = v.findViewById(R.id.spinner_district);

        // Headers
        setupHeader(v.findViewById(R.id.header_identity), "🏢", "Business Identity", "Basic business identification details");
        setupHeader(v.findViewById(R.id.header_license), "📄", "License & Registration", "Trade license, BIN and dates");
        setupHeader(v.findViewById(R.id.header_contact), "📍", "Contact & Location", "Contact details and business address");
        setupHeader(v.findViewById(R.id.header_financial), "📊", "Financial & Workforce", "Turnover and employee information");
    }

    private void setupHeader(View v, String icon, String title, String sub) {
        ((TextView)v.findViewById(R.id.tv_section_icon)).setText(icon);
        ((TextView)v.findViewById(R.id.tv_section_title)).setText(title);
        ((TextView)v.findViewById(R.id.tv_section_sub)).setText(sub);
    }

    private void setupDropdowns() {
        setupDropdown(etType, new String[]{"Sole Proprietorship", "Partnership", "Private Limited"});
        setupDropdown(etCategory, new String[]{"Manufacturing", "Trading", "Service"});
        setupDropdown(etDivision, new String[]{"Dhaka", "Chittagong", "Rajshahi"});
        setupDropdown(etDistrict, new String[]{"Dhaka", "Gazipur", "Narayanganj"});
    }

    private void setupDropdown(AutoCompleteTextView et, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, options);
        et.setAdapter(adapter);
    }

    private void setupDatePickers() {
        etIncorporation.setOnClickListener(v -> showDatePicker(etIncorporation));
        etRegistration.setOnClickListener(v -> showDatePicker(etRegistration));
        etExpiry.setOnClickListener(v -> showDatePicker(etExpiry));
    }

    private void showDatePicker(EditText et) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, day) -> {
            String date = String.format(Locale.US, "%d-%02d-%02d", year, month + 1, day);
            et.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadProfile() {
        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Taxpayer> resp = RetrofitClient.taxpayerApi(tm).getMyProfile().execute();
                requireActivity().runOnUiThread(() -> {
                    if (resp.isSuccessful() && resp.body() != null) {
                        profile = resp.body();
                        tvTpName.setText(profile.getFullName());
                        tvTpTin.setText("TIN: " + profile.getTinNumber());
                        etTin.setText(profile.getTinNumber());
                        etOwner.setText(profile.getFullName());
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void submit() {
        String name = etName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Business Name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("businessName", name);
        body.put("tinNumber", etTin.getText().toString());
        body.put("ownerName", etOwner.getText().toString());
        body.put("tradeLicenseNo", etTradeLicense.getText().toString());
        body.put("incorporationDate", etIncorporation.getText().toString());
        body.put("registrationDate", etRegistration.getText().toString());
        body.put("expiryDate", etExpiry.getText().toString());
        body.put("email", etEmail.getText().toString());
        body.put("phone", etPhone.getText().toString());
        body.put("address", etAddress.getText().toString());
        body.put("annualTurnover", Double.parseDouble(etTurnover.getText().toString().isEmpty() ? "0" : etTurnover.getText().toString()));
        body.put("numberOfEmployees", Integer.parseInt(etEmployees.getText().toString().isEmpty() ? "0" : etEmployees.getText().toString()));
        body.put("remarks", etRemarks.getText().toString());
        body.put("status", "Active");
        
        // Simplified mapping for type/category
        Map<String, Object> bt = new HashMap<>(); bt.put("id", 1); body.put("businessType", bt);
        Map<String, Object> bc = new HashMap<>(); bc.put("id", 1); body.put("businessCategory", bc);
        Map<String, Object> dv = new HashMap<>(); dv.put("id", 1); body.put("division", dv);
        Map<String, Object> ds = new HashMap<>(); ds.put("id", 1); body.put("district", ds);
        Map<String, Object> tp = new HashMap<>(); tp.put("id", profile.getId()); body.put("taxpayer", tp);

        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Business> resp = RetrofitClient.businessApi(tm).registerBusiness(body).execute();
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (resp.isSuccessful()) {
                        Toast.makeText(requireContext(), "Business registered successfully", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed();
                    } else {
                        Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
