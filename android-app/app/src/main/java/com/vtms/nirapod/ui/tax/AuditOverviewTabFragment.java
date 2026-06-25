package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.AuditCase;

public class AuditOverviewTabFragment extends Fragment {

    private static final String ARG_AUDIT = "audit";
    private AuditCase audit;

    public static AuditOverviewTabFragment newInstance(AuditCase audit) {
        AuditOverviewTabFragment f = new AuditOverviewTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_AUDIT, audit);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            audit = (AuditCase) getArguments().getSerializable(ARG_AUDIT);
        }
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_tab_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (audit == null) return;

        ((TextView)v.findViewById(R.id.tv_val_case_no)).setText(audit.getCaseNo());
        ((TextView)v.findViewById(R.id.tv_val_audit_type)).setText(audit.getAuditType());
        ((TextView)v.findViewById(R.id.tv_val_tax_type)).setText(audit.getTaxType());
        ((TextView)v.findViewById(R.id.tv_val_fiscal_year)).setText(audit.getFiscalYear());
        ((TextView)v.findViewById(R.id.tv_val_period)).setText(String.format("%s - %s", audit.getTaxPeriodStart(), audit.getTaxPeriodEnd()));
        ((TextView)v.findViewById(R.id.tv_val_status)).setText(audit.getStatus().replace("_", " "));

        ((TextView)v.findViewById(R.id.tv_val_officer)).setText(audit.getAssignedOfficerName());
        ((TextView)v.findViewById(R.id.tv_val_supervisor)).setText(audit.getSupervisorName());
        ((TextView)v.findViewById(R.id.tv_val_due_date)).setText(audit.getDueDate());
        ((TextView)v.findViewById(R.id.tv_val_notice_date)).setText(audit.getCreatedAt());
    }
}
