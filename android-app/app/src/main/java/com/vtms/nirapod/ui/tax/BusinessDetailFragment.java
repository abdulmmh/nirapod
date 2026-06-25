package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Business;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class BusinessDetailFragment extends Fragment {

    private static final String ARG_BUSINESS_ID = "business_id";
    private long businessId;
    private View pbLoading;

    // Header views
    private TextView tvHeaderRegNo, tvHeaderName, tvHeaderIcon, tvHeaderType, tvHeaderStatus, tvHeaderTurnover, tvHeaderEmployees;
    
    // Value views
    private TextView tvValName, tvValOwner, tvValTin, tvValType, tvValCategory;
    private TextView tvValRegNo, tvValTradeLicense, tvValIncorporation, tvValRegistration, tvValExpiry;
    private TextView tvValEmail, tvValPhone, tvValDivision, tvValDistrict, tvValAddress;
    private TextView tvValTurnover, tvValEmployees, tvValStatus;

    public static BusinessDetailFragment newInstance(long id) {
        BusinessDetailFragment f = new BusinessDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BUSINESS_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            businessId = getArguments().getLong(ARG_BUSINESS_ID);
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bindViews(view);
        setupHeaders(view);
        loadData();

        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void bindViews(View v) {
        pbLoading = v.findViewById(R.id.pb_loading);

        tvHeaderRegNo    = v.findViewById(R.id.tv_header_reg_no);
        tvHeaderName     = v.findViewById(R.id.tv_header_name);
        tvHeaderIcon     = v.findViewById(R.id.tv_header_icon);
        tvHeaderType     = v.findViewById(R.id.tv_header_type);
        tvHeaderStatus   = v.findViewById(R.id.tv_header_status);
        tvHeaderTurnover = v.findViewById(R.id.tv_header_turnover);
        tvHeaderEmployees = v.findViewById(R.id.tv_header_employees);

        // Card 1
        View c1 = v.findViewById(R.id.card_info);
        tvValName     = c1.findViewById(R.id.tv_val_business_name);
        tvValOwner    = c1.findViewById(R.id.tv_val_owner_name);
        tvValTin      = c1.findViewById(R.id.tv_val_tin);
        tvValType     = c1.findViewById(R.id.tv_val_type);
        tvValCategory = c1.findViewById(R.id.tv_val_category);

        // Card 2
        View c2 = v.findViewById(R.id.card_license);
        tvValRegNo          = c2.findViewById(R.id.tv_val_reg_no);
        tvValTradeLicense   = c2.findViewById(R.id.tv_val_trade_license);
        tvValIncorporation = c2.findViewById(R.id.tv_val_incorporation);
        tvValRegistration   = c2.findViewById(R.id.tv_val_registration);
        tvValExpiry         = c2.findViewById(R.id.tv_val_expiry);

        // Card 3
        View c3 = v.findViewById(R.id.card_location);
        tvValEmail    = c3.findViewById(R.id.tv_val_email);
        tvValPhone    = c3.findViewById(R.id.tv_val_phone);
        tvValDivision = c3.findViewById(R.id.tv_val_division);
        tvValDistrict = c3.findViewById(R.id.tv_val_district);
        tvValAddress  = c3.findViewById(R.id.tv_val_address);

        // Card 4
        View c4 = v.findViewById(R.id.card_financial);
        tvValTurnover  = c4.findViewById(R.id.tv_val_turnover);
        tvValEmployees = c4.findViewById(R.id.tv_val_employees);
        tvValStatus    = c4.findViewById(R.id.tv_val_status);
    }

    private void setupHeaders(View v) {
        setupHeader(v.findViewById(R.id.card_info).findViewById(R.id.section_header), "🏢", "Business Information", "Basic identification details");
        setupHeader(v.findViewById(R.id.card_license).findViewById(R.id.section_header), "📄", "License & Registration", "Trade license and registration dates");
        setupHeader(v.findViewById(R.id.card_location).findViewById(R.id.section_header), "📍", "Contact & Location", "Business contact and address");
        setupHeader(v.findViewById(R.id.card_financial).findViewById(R.id.section_header), "📊", "Financial & Workforce", "Turnover and employee information");
    }

    private void setupHeader(View headerView, String icon, String title, String sub) {
        if (headerView == null) return;
        TextView tvIcon = headerView.findViewById(R.id.tv_section_icon);
        TextView tvTitle = headerView.findViewById(R.id.tv_section_title);
        TextView tvSub = headerView.findViewById(R.id.tv_section_sub);
        if (tvIcon != null) tvIcon.setText(icon);
        if (tvTitle != null) tvTitle.setText(title);
        if (tvSub != null) tvSub.setText(sub);
    }

    private void loadData() {
        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Business> response = RetrofitClient.businessApi(tm).getBusinessById(businessId).execute();
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        bindBusiness(response.body());
                    } else {
                        Toast.makeText(requireContext(), "Failed to load business details", Toast.LENGTH_SHORT).show();
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

    private void bindBusiness(Business b) {
        // Header
        tvHeaderRegNo.setText(b.getBusinessRegNo() != null ? b.getBusinessRegNo() : "---");
        tvHeaderName.setText(b.getBusinessName());
        tvHeaderType.setText(b.getBusinessTypeName());
        tvHeaderStatus.setText(b.getStatus());
        tvHeaderTurnover.setText(formatCurrency(b.getAnnualTurnover()));
        tvHeaderEmployees.setText(String.valueOf(b.getNumberOfEmployees() != null ? b.getNumberOfEmployees() : 0));

        String cat = b.getBusinessCategoryName();
        if (cat != null) {
            if (cat.contains("Manufacturing")) tvHeaderIcon.setText("🏭");
            else if (cat.contains("Trading"))  tvHeaderIcon.setText("🛍️");
            else if (cat.contains("Service"))  tvHeaderIcon.setText("💼");
            else tvHeaderIcon.setText("🏢");
        }

        // Values
        tvValName.setText(b.getBusinessName());
        tvValOwner.setText(b.getOwnerName() != null ? b.getOwnerName() : "---");
        tvValTin.setText(b.getTinNumber() != null ? b.getTinNumber() : "---");
        tvValType.setText(b.getBusinessTypeName());
        tvValCategory.setText(b.getBusinessCategoryName());

        tvValRegNo.setText(b.getBusinessRegNo() != null ? b.getBusinessRegNo() : "---");
        tvValTradeLicense.setText(b.getTradeLicenseNo() != null ? b.getTradeLicenseNo() : "---");
        tvValIncorporation.setText(b.getIncorporationDate() != null ? b.getIncorporationDate() : "---");
        tvValRegistration.setText(b.getRegistrationDate() != null ? b.getRegistrationDate() : "---");
        tvValExpiry.setText(b.getExpiryDate() != null ? b.getExpiryDate() : "---");

        tvValEmail.setText(b.getEmail() != null ? b.getEmail() : "---");
        tvValPhone.setText(b.getPhone() != null ? b.getPhone() : "---");
        tvValDivision.setText(b.getDivisionName());
        tvValDistrict.setText(b.getDistrictName());
        tvValAddress.setText(b.getAddress() != null ? b.getAddress() : "---");

        tvValTurnover.setText(formatCurrency(b.getAnnualTurnover()));
        tvValEmployees.setText(String.valueOf(b.getNumberOfEmployees() != null ? b.getNumberOfEmployees() : 0));
        tvValStatus.setText(b.getStatus());

        // Status color
        int color;
        String status = b.getStatus();
        if ("Active".equals(status)) {
            color = requireContext().getColor(R.color.success);
        } else if ("Pending".equals(status)) {
            color = requireContext().getColor(R.color.warning);
        } else {
            color = requireContext().getColor(R.color.error);
        }
        tvHeaderStatus.getBackground().setTint(color);
        tvValStatus.getBackground().setTint(color);
    }

    private String formatCurrency(double amount) {
        if (amount >= 10000000) return String.format(Locale.getDefault(), "৳%.2f Cr", amount / 10000000);
        if (amount >= 100000)   return String.format(Locale.getDefault(), "৳%.2f L", amount / 100000);
        return String.format(Locale.getDefault(), "৳%,.0f", amount);
    }
}
