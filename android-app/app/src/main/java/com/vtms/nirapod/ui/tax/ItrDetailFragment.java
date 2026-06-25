package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.ItrAction;
import com.vtms.nirapod.data.model.ItrRecord;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class ItrDetailFragment extends Fragment {

    private ItrRecord itrRecord;

    // Header Views
    private TextView tvHeaderReturnNo, tvHeaderTaxpayerName, tvHeaderCategory, tvHeaderYear, tvHeaderStatus;
    private TextView tvHeaderNetPayable, tvHeaderRefundable;
    private View layoutHeaderRefundable;

    // KPI Views
    private TextView tvKpiGrossIncome, tvKpiTaxableIncome, tvKpiGrossTax, tvLabelGrossTax;

    // Value Views
    private TextView tvValReturnNo, tvValTin, tvValTaxpayer, tvValIncomeYear;
    private TextView tvValDueDate, tvValSubmittedOn, tvValStatus;
    private TextView tvValGrossIncome, tvValExempt, tvValGrossTaxVal, tvValRebate;
    private TextView tvValAdvanceTax, tvValWithholding, tvValTaxPaid;

    // History & Loading
    private LinearLayout containerHistory;
    private View tvNoHistory, pbLoading;

    public static ItrDetailFragment newInstance(ItrRecord record) {
        ItrDetailFragment f = new ItrDetailFragment();
        f.itrRecord = record;
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itr_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setupSectionHeaders();
        
        // Initial binding from list record
        bindRecord(itrRecord);
        
        // Load full detail
        loadFullDetail();
    }

    private void bindViews(View v) {
        tvHeaderReturnNo      = v.findViewById(R.id.tv_header_return_no);
        tvHeaderTaxpayerName  = v.findViewById(R.id.tv_header_taxpayer_name);
        tvHeaderCategory      = v.findViewById(R.id.tv_header_category);
        tvHeaderYear          = v.findViewById(R.id.tv_header_year);
        tvHeaderStatus        = v.findViewById(R.id.tv_header_status);
        tvHeaderNetPayable    = v.findViewById(R.id.tv_header_net_payable);
        tvHeaderRefundable    = v.findViewById(R.id.tv_header_refundable);
        layoutHeaderRefundable = v.findViewById(R.id.layout_header_refundable);

        tvKpiGrossIncome     = v.findViewById(R.id.tv_kpi_gross_income);
        tvKpiTaxableIncome   = v.findViewById(R.id.tv_kpi_taxable_income);
        tvKpiGrossTax        = v.findViewById(R.id.tv_kpi_gross_tax);
        tvLabelGrossTax      = v.findViewById(R.id.tv_label_gross_tax);

        tvValReturnNo        = v.findViewById(R.id.tv_val_return_no);
        tvValTin             = v.findViewById(R.id.tv_val_tin);
        tvValTaxpayer        = v.findViewById(R.id.tv_val_taxpayer);
        tvValIncomeYear      = v.findViewById(R.id.tv_val_income_year);

        tvValDueDate         = v.findViewById(R.id.tv_val_due_date);
        tvValSubmittedOn     = v.findViewById(R.id.tv_val_submitted_on);
        tvValStatus          = v.findViewById(R.id.tv_val_status);

        tvValGrossIncome     = v.findViewById(R.id.tv_val_gross_income);
        tvValExempt          = v.findViewById(R.id.tv_val_exempt);
        tvValGrossTaxVal     = v.findViewById(R.id.tv_val_gross_tax);
        tvValRebate          = v.findViewById(R.id.tv_val_rebate);
        tvValAdvanceTax      = v.findViewById(R.id.tv_val_advance_tax);
        tvValWithholding     = v.findViewById(R.id.tv_val_withholding);
        tvValTaxPaid         = v.findViewById(R.id.tv_val_tax_paid);

        containerHistory     = v.findViewById(R.id.container_history);
        tvNoHistory          = v.findViewById(R.id.tv_no_history);
        pbLoading            = v.findViewById(R.id.pb_loading);
        View btnDownloadAck  = v.findViewById(R.id.btn_download_ack);

        btnDownloadAck.setOnClickListener(view -> 
            Toast.makeText(requireContext(), "Download Acknowledgement coming soon", Toast.LENGTH_SHORT).show());
    }

    private void setupSectionHeaders() {
        setupHeader(getView().findViewById(R.id.header_taxpayer_info), "👤", "Taxpayer & Period", "Identity and filing period");
        setupHeader(getView().findViewById(R.id.header_submission_details), "📅", "Submission Details", "Dates and status");
        setupHeader(getView().findViewById(R.id.header_tax_breakdown), "📊", "Income & Tax Breakdown", "Financial calculations");
        setupHeader(getView().findViewById(R.id.header_action_history), "🕒", "Action History", "Workflow log");
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

    private void bindRecord(ItrRecord r) {
        if (r == null) return;

        // Header
        tvHeaderReturnNo.setText(r.getReturnNo() != null ? r.getReturnNo() : "---");
        tvHeaderTaxpayerName.setText(r.getTaxpayerName() != null ? r.getTaxpayerName() : "---");
        tvHeaderCategory.setText(r.getItrCategory());
        tvHeaderYear.setText("AY " + r.getAssessmentYear());
        tvHeaderStatus.setText(r.getStatus());
        applyStatusColor(tvHeaderStatus, r.getStatus());

        tvHeaderNetPayable.setText(fmt(r.getNetTaxPayable()));
        if (r.getRefundable() > 0) {
            tvHeaderRefundable.setText(fmt(r.getRefundable()));
            layoutHeaderRefundable.setVisibility(View.VISIBLE);
        } else {
            layoutHeaderRefundable.setVisibility(View.GONE);
        }

        // KPIs
        tvKpiGrossIncome.setText(fmtCompact(r.getGrossIncome()));
        tvKpiTaxableIncome.setText(fmtCompact(r.getTaxableIncome()));
        tvKpiGrossTax.setText(fmt(r.getGrossTax()));
        if (r.getTaxRate() > 0) {
            tvLabelGrossTax.setText(String.format(Locale.getDefault(), "Gross Tax (%.2f%%)", r.getTaxRate()));
        }

        // Section: Taxpayer
        tvValReturnNo.setText(r.getReturnNo());
        tvValTin.setText(r.getTinNumber());
        tvValTaxpayer.setText(r.getTaxpayerName());
        tvValIncomeYear.setText(r.getIncomeYear());

        // Section: Submission
        tvValDueDate.setText(r.getDueDate());
        tvValSubmittedOn.setText(r.getSubmissionDate());
        tvValStatus.setText(r.getStatus());
        applyStatusColor(tvValStatus, r.getStatus());

        // Section: Breakdown
        tvValGrossIncome.setText(fmt(r.getGrossIncome()));
        tvValExempt.setText("− " + fmt(r.getExemptIncome()));
        tvValGrossTaxVal.setText(fmt(r.getGrossTax()));
        tvValRebate.setText("− " + fmt(r.getTaxRebate()));
        tvValAdvanceTax.setText(fmt(r.getAdvanceTaxPaid()));
        tvValWithholding.setText(fmt(r.getWithholdingTax()));
        tvValTaxPaid.setText(fmt(r.getTaxPaid()));
    }

    private void loadFullDetail() {
        TokenManager tm = new TokenManager(requireContext());
        pbLoading.setVisibility(View.VISIBLE);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<ItrRecord> resp = RetrofitClient.itrApi(tm)
                        .getReturnById(itrRecord.getId()).execute();

                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (resp.isSuccessful() && resp.body() != null) {
                        itrRecord = resp.body();
                        bindRecord(itrRecord);
                        renderHistory(itrRecord.getActionHistory());
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> pbLoading.setVisibility(View.GONE));
            }
        });
    }

    private void renderHistory(List<ItrAction> history) {
        containerHistory.removeAllViews();
        if (history == null || history.isEmpty()) {
            tvNoHistory.setVisibility(View.VISIBLE);
            return;
        }
        tvNoHistory.setVisibility(View.GONE);

        for (int i = 0; i < history.size(); i++) {
            ItrAction action = history.get(i);
            View itemView = getLayoutInflater().inflate(R.layout.item_itr_action, containerHistory, false);
            
            TextView tvTitle = itemView.findViewById(R.id.tv_action_title);
            TextView tvFrom = itemView.findViewById(R.id.tv_action_from);
            TextView tvTo = itemView.findViewById(R.id.tv_action_to);
            TextView tvUser = itemView.findViewById(R.id.tv_action_user);
            TextView tvRole = itemView.findViewById(R.id.tv_action_role);
            TextView tvRemarks = itemView.findViewById(R.id.tv_action_remarks);
            TextView tvActionTime = itemView.findViewById(R.id.tv_action_time);

            tvTitle.setText(action.getAction());
            tvFrom.setText(action.getFromStatus() != null ? action.getFromStatus() : "---");
            tvTo.setText(action.getToStatus());
            tvUser.setText("👤 " + action.getPerformedBy());
            tvRole.setText(action.getRole());
            
            if (action.getRemarks() != null && !action.getRemarks().isEmpty()) {
                tvRemarks.setText(action.getRemarks());
                tvRemarks.setVisibility(View.VISIBLE);
            }
            
            tvActionTime.setText(action.getPerformedAt());

            // Timeline UI
            View line = itemView.findViewById(R.id.tl_line);
            if (i == history.size() - 1) {
                line.setVisibility(View.GONE);
            }

            containerHistory.addView(itemView);
        }
    }

    private void applyStatusColor(TextView tv, String status) {
        int color;
        if ("Accepted".equals(status) || "APPROVED".equals(status)) {
            color = ContextCompat.getColor(requireContext(), R.color.success);
        } else if ("Rejected".equals(status)) {
            color = ContextCompat.getColor(requireContext(), R.color.error);
        } else if ("Under Review".equals(status)) {
            color = ContextCompat.getColor(requireContext(), R.color.warning);
        } else {
            color = ContextCompat.getColor(requireContext(), R.color.primary);
        }
        tv.getBackground().setTint(color);
    }

    private String fmt(double val) {
        return "BDT " + String.format(Locale.getDefault(), "%,.2f", val);
    }

    private String fmtCompact(double val) {
        if (val >= 10000000) return "BDT " + String.format(Locale.getDefault(), "%.2f Cr", val / 10000000);
        if (val >= 100000) return "BDT " + String.format(Locale.getDefault(), "%.2f L", val / 100000);
        return "BDT " + String.format(Locale.getDefault(), "%,.0f", val);
    }
}
