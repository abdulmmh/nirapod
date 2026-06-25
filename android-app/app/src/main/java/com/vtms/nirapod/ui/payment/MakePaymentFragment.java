package com.vtms.nirapod.ui.payment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.OutstandingItem;
import com.vtms.nirapod.data.model.Payment;
import com.vtms.nirapod.data.model.Taxpayer;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

import retrofit2.Response;

public class MakePaymentFragment extends Fragment {

    // ── Views ──────────────────────────────────────────────────────────────

    private ProgressBar pbProfileLoad;
    private TextView    tvProfileError;
    private View     cardTaxpayer;
    private TextView tvTaxpayerName, tvTaxpayerMeta;
    private View         sectionOutstanding;
    private RecyclerView rvOutstanding;
    private TextView     tvOutstandingEmpty;
    private ProgressBar  pbOutstanding;

    private View              sectionForm;
    private TextInputEditText etTinNumber, etTaxpayerName;
    private AutoCompleteTextView etPaymentType;
    private TextInputEditText etReturnNo;

    private TextInputEditText etAmount, etReferenceNo;
    private AutoCompleteTextView etPaymentMethod;
    private TextInputEditText etPaymentDate, etValueDate;

    private AutoCompleteTextView etBankName;
    private TextInputEditText etBankBranch, etAccountNo;
    private TextInputEditText etRemarks;

    private MaterialButton btnRecord, btnCancel, btnReset;
    private ProgressBar    pbSubmit;
    private TextView       tvSubmitError;

    // ── State ──────────────────────────────────────────────────────────────

    private TokenManager       tm;
    private Taxpayer           selectedTaxpayer;
    private OutstandingAdapter outstandingAdapter;

    private static final String[] PAYMENT_TYPES = {
            "Income Tax", "VAT", "AIT", "Penalty", "Demand Notice", "Other"
    };

    private static final String[] PAYMENT_METHODS = {
            "Bank Transfer", "Online Transfer", "Cheque",
            "Cash", "NPSB", "RTGS", "EFT", "Mobile Banking"
    };

    private static final String[] BANKS = {
            "Sonali Bank", "Janata Bank", "Agrani Bank", "Rupali Bank",
            "Dutch Bangla Bank", "BRAC Bank", "Islami Bank", "Eastern Bank",
            "Prime Bank", "Southeast Bank", "Pubali Bank", "AB Bank",
            "City Bank", "Mutual Trust Bank", "Other"
    };

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tm = new TokenManager(requireContext());
        bindViews(view);
        setupDropdowns();
        setupDatePickers();
        setupOutstandingList();
        setupClickListeners();
        setTodayDates();
        showFormSection(false);
        loadMyProfile();
    }

    private void bindViews(View v) {
        pbProfileLoad   = v.findViewById(R.id.pbProfileLoad);
        tvProfileError  = v.findViewById(R.id.tvProfileError);
        cardTaxpayer    = v.findViewById(R.id.cardTaxpayer);
        tvTaxpayerName  = v.findViewById(R.id.tvTaxpayerName);
        tvTaxpayerMeta  = v.findViewById(R.id.tvTaxpayerMeta);
        sectionOutstanding = v.findViewById(R.id.sectionOutstanding);
        rvOutstanding      = v.findViewById(R.id.rvOutstanding);
        tvOutstandingEmpty = v.findViewById(R.id.tvOutstandingEmpty);
        pbOutstanding      = v.findViewById(R.id.pbOutstanding);
        sectionForm       = v.findViewById(R.id.sectionForm);
        etTinNumber       = v.findViewById(R.id.etTinNumber);
        etTaxpayerName    = v.findViewById(R.id.etTaxpayerName);
        etPaymentType     = v.findViewById(R.id.etPaymentType);
        etReturnNo        = v.findViewById(R.id.etReturnNo);
        etAmount          = v.findViewById(R.id.etAmount);
        etReferenceNo     = v.findViewById(R.id.etReferenceNo);
        etPaymentMethod   = v.findViewById(R.id.etPaymentMethod);
        etPaymentDate     = v.findViewById(R.id.etPaymentDate);
        etValueDate       = v.findViewById(R.id.etValueDate);
        etBankName        = v.findViewById(R.id.etBankName);
        etBankBranch      = v.findViewById(R.id.etBankBranch);
        etAccountNo       = v.findViewById(R.id.etAccountNo);
        etRemarks         = v.findViewById(R.id.etRemarks);
        btnRecord         = v.findViewById(R.id.btnRecord);
        btnCancel         = v.findViewById(R.id.btnCancel);
        btnReset          = v.findViewById(R.id.btnReset);
        pbSubmit          = v.findViewById(R.id.pbSubmit);
        tvSubmitError     = v.findViewById(R.id.tvSubmitError);
    }

    private void setupDropdowns() {
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, PAYMENT_TYPES);
        etPaymentType.setAdapter(typeAdapter);

        ArrayAdapter<String> methodAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, PAYMENT_METHODS);
        etPaymentMethod.setAdapter(methodAdapter);

        ArrayAdapter<String> bankAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, BANKS);
        etBankName.setAdapter(bankAdapter);
    }

    private void setupDatePickers() {
        etPaymentDate.setOnClickListener(v -> showDatePicker(etPaymentDate));
        etValueDate.setOnClickListener(v -> showDatePicker(etValueDate));
    }

    private void showDatePicker(TextInputEditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (dp, y, m, d) ->
                target.setText(String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void setTodayDates() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
        etPaymentDate.setText(today);
        etValueDate.setText(today);
    }

    private void setupOutstandingList() {
        outstandingAdapter = new OutstandingAdapter(new OutstandingAdapter.OnSelectListener() {
            @Override
            public void onSelect(OutstandingItem item) {
                etPaymentType.setText(item.getType(), false);
                etReturnNo.setText(item.getReturnNo());
                etAmount.setText(String.valueOf((long) item.getOutstanding()));
            }

            @Override
            public void onUnselect() {
                etPaymentType.setText("", false);
                etReturnNo.setText("");
                etAmount.setText("");
            }
        });
        rvOutstanding.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOutstanding.setAdapter(outstandingAdapter);
        rvOutstanding.setNestedScrollingEnabled(false);
    }

    private void setupClickListeners() {
        btnRecord.setOnClickListener(v -> doSubmit());
        btnCancel.setOnClickListener(v -> requireActivity().onBackPressed());
        btnReset.setOnClickListener(v -> doReset());
    }

    private void loadMyProfile() {
        pbProfileLoad.setVisibility(View.VISIBLE);
        tvProfileError.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Taxpayer> resp = RetrofitClient.taxpayerApi(tm).getMyProfile().execute();
                requireActivity().runOnUiThread(() -> {
                    pbProfileLoad.setVisibility(View.GONE);
                    if (resp.isSuccessful() && resp.body() != null) {
                        onTaxpayerFound(resp.body());
                    } else {
                        tvProfileError.setText("Could not load profile (code: " + resp.code() + ")");
                        tvProfileError.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pbProfileLoad.setVisibility(View.GONE);
                    tvProfileError.setText("Cannot connect to server");
                    tvProfileError.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void onTaxpayerFound(Taxpayer taxpayer) {
        selectedTaxpayer = taxpayer;
        tvTaxpayerName.setText(taxpayer.getDisplayName());
        String meta = "TIN: " + taxpayer.getTinNumber();
        if (taxpayer.getPhone() != null && !taxpayer.getPhone().isEmpty()) meta += " · " + taxpayer.getPhone();
        tvTaxpayerMeta.setText(meta);
        cardTaxpayer.setVisibility(View.VISIBLE);
        etTinNumber.setText(taxpayer.getTinNumber());
        etTaxpayerName.setText(taxpayer.getDisplayName());
        showFormSection(true);
        loadOutstandingItems(taxpayer.getId());
    }

    private void loadOutstandingItems(long taxpayerId) {
        pbOutstanding.setVisibility(View.VISIBLE);
        tvOutstandingEmpty.setVisibility(View.GONE);
        rvOutstanding.setVisibility(View.GONE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<java.util.List<OutstandingItem>> resp =
                        RetrofitClient.paymentApi(tm).getOutstanding(taxpayerId).execute();

                requireActivity().runOnUiThread(() -> {
                    pbOutstanding.setVisibility(View.GONE);
                    if (resp.isSuccessful() && resp.body() != null && !resp.body().isEmpty()) {
                        outstandingAdapter.setItems(resp.body());
                        rvOutstanding.setVisibility(View.VISIBLE);
                    } else {
                        tvOutstandingEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pbOutstanding.setVisibility(View.GONE);
                    tvOutstandingEmpty.setText("Could not load outstanding dues");
                    tvOutstandingEmpty.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void showFormSection(boolean show) {
        int vis = show ? View.VISIBLE : View.GONE;
        sectionOutstanding.setVisibility(vis);
        sectionForm.setVisibility(vis);
        if (!show) cardTaxpayer.setVisibility(View.GONE);
    }

    private void doSubmit() {
        if (!validateForm()) return;

        tvSubmitError.setVisibility(View.GONE);
        pbSubmit.setVisibility(View.VISIBLE);
        btnRecord.setEnabled(false);

        Payment payment = buildPayment();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Payment> resp = RetrofitClient.paymentApi(tm).createPayment(payment).execute();
                requireActivity().runOnUiThread(() -> {
                    pbSubmit.setVisibility(View.GONE);
                    btnRecord.setEnabled(true);
                    if (resp.isSuccessful() && resp.body() != null) {
                        Toast.makeText(requireContext(), "✅ Payment recorded successfully! TXN: " + resp.body().getTransactionId(), Toast.LENGTH_LONG).show();
                        requireActivity().onBackPressed();
                    } else {
                        tvSubmitError.setText("Could not record payment (code: " + resp.code() + ")");
                        tvSubmitError.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pbSubmit.setVisibility(View.GONE);
                    btnRecord.setEnabled(true);
                    tvSubmitError.setText("Server error: " + e.getMessage());
                    tvSubmitError.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private boolean validateForm() {
        if (selectedTaxpayer == null) {
            Toast.makeText(requireContext(), "Profile not loaded, please wait", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(getText(etAmount))) {
            etAmount.setError("Enter amount");
            return false;
        }
        if (TextUtils.isEmpty(etPaymentType.getText())) {
            Toast.makeText(requireContext(), "Select Payment Type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etPaymentMethod.getText())) {
            Toast.makeText(requireContext(), "Select Payment Method", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(etBankName.getText())) {
            Toast.makeText(requireContext(), "Select Bank", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Payment buildPayment() {
        Payment p = new Payment();
        p.setTaxpayerId(selectedTaxpayer.getId());
        p.setTinNumber(selectedTaxpayer.getTinNumber());
        p.setTaxpayerName(selectedTaxpayer.getDisplayName());
        p.setPaymentType(etPaymentType.getText().toString());
        p.setPaymentMethod(etPaymentMethod.getText().toString());
        p.setAmount(Double.parseDouble(getText(etAmount)));
        p.setReturnNo(nullIfEmpty(getText(etReturnNo)));
        p.setReferenceNo(nullIfEmpty(getText(etReferenceNo)));
        p.setPaymentDate(getText(etPaymentDate));
        p.setValueDate(nullIfEmpty(getText(etValueDate)));
        p.setBankName(etBankName.getText().toString());
        p.setBankBranch(nullIfEmpty(getText(etBankBranch)));
        p.setAccountNo(nullIfEmpty(getText(etAccountNo)));
        p.setRemarks(nullIfEmpty(getText(etRemarks)));
        return p;
    }

    private void doReset() {
        etAmount.setText("");
        etReturnNo.setText("");
        etReferenceNo.setText("");
        etBankBranch.setText("");
        etAccountNo.setText("");
        etRemarks.setText("");
        etPaymentType.setText("", false);
        etPaymentMethod.setText("", false);
        etBankName.setText("", false);
        setTodayDates();
        tvSubmitError.setVisibility(View.GONE);
        outstandingAdapter.clearSelection();
    }

    private String getText(EditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private String nullIfEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
