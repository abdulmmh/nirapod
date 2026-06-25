package com.vtms.nirapod.ui.payment;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.Payment;
import java.util.Locale;

public class PaymentDetailFragment extends Fragment {

    private static final String ARG_PAYMENT = "payment";
    private Payment payment;

    public static PaymentDetailFragment newInstance(Payment p) {
        PaymentDetailFragment f = new PaymentDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAYMENT, p);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            payment = (Payment) getArguments().getSerializable(ARG_PAYMENT);
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (payment == null) return;

        bindData(view);
        view.findViewById(R.id.btn_back).setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    private void bindData(View v) {
        // Header
        ((TextView)v.findViewById(R.id.tv_header_txn_id)).setText(payment.getTransactionId());
        ((TextView)v.findViewById(R.id.tv_header_type)).setText(payment.getPaymentType());
        ((TextView)v.findViewById(R.id.tv_header_date)).setText(String.format("📅 %s", payment.getPaymentDate()));
        ((TextView)v.findViewById(R.id.tv_header_amount)).setText(format(payment.getAmount()));
        ((TextView)v.findViewById(R.id.tv_header_status)).setText(payment.getStatus());

        // Taxpayer Info
        ((TextView)v.findViewById(R.id.tv_val_tin)).setText(payment.getTinNumber());
        ((TextView)v.findViewById(R.id.tv_val_taxpayer)).setText(payment.getTaxpayerName());
        ((TextView)v.findViewById(R.id.tv_val_ref_no)).setText(payment.getReturnNo() != null ? payment.getReturnNo() : "—");

        // Payment Info
        ((TextView)v.findViewById(R.id.tv_val_pay_type)).setText(payment.getPaymentType());
        ((TextView)v.findViewById(R.id.tv_val_method)).setText(payment.getPaymentMethod());
        ((TextView)v.findViewById(R.id.tv_val_amount)).setText(format(payment.getAmount()));
        ((TextView)v.findViewById(R.id.tv_val_date)).setText(payment.getPaymentDate());
        ((TextView)v.findViewById(R.id.tv_val_value_date)).setText(payment.getValueDate() != null ? payment.getValueDate() : "—");
        ((TextView)v.findViewById(R.id.tv_val_reference)).setText(payment.getReferenceNo() != null ? payment.getReferenceNo() : "—");

        // Bank Info
        ((TextView)v.findViewById(R.id.tv_val_bank)).setText(payment.getBankName());
        ((TextView)v.findViewById(R.id.tv_val_branch)).setText(payment.getBankBranch() != null ? payment.getBankBranch() : "—");
        ((TextView)v.findViewById(R.id.tv_val_account)).setText(payment.getAccountNo() != null ? payment.getAccountNo() : "—");

        // Processing Info
        ((TextView)v.findViewById(R.id.tv_val_status)).setText(payment.getStatus());
        ((TextView)v.findViewById(R.id.tv_val_processed_by)).setText(payment.getProcessedBy() != null ? payment.getProcessedBy() : "System");
        ((TextView)v.findViewById(R.id.tv_val_created_at)).setText(payment.getCreatedAt());
        
        // Color status badge
        int color = "Completed".equalsIgnoreCase(payment.getStatus()) ? R.color.success : R.color.primary;
        v.findViewById(R.id.tv_header_status).getBackground().setTint(requireContext().getColor(color));
    }

    private String format(double amount) {
        return String.format(Locale.US, "৳%,.2f", amount);
    }
}
