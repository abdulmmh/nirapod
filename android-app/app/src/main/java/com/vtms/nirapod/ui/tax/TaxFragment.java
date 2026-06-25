package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;

public class TaxFragment extends Fragment {

    public static final String ARG_START_TAB = "start_tab";
    public static final int TAB_ITR = 0;
    public static final int TAB_AIT = 1;
    public static final int TAB_CERTIFICATE = 2;
    public static final int TAB_BUSINESS = 3;
    public static final int TAB_AUDITS = 4;
    public static final int TAB_APPEALS = 5;

    public static TaxFragment newInstance(int startTab) {
        TaxFragment f = new TaxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_START_TAB, startTab);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tax, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnItr         = view.findViewById(R.id.btnItr);
        Button btnAit         = view.findViewById(R.id.btnAit);
        Button btnCertificate = view.findViewById(R.id.btnCertificate);
        Button btnBusiness    = view.findViewById(R.id.btnBusiness);
        Button btnAudits      = view.findViewById(R.id.btnAudits);
        Button btnAppeals     = view.findViewById(R.id.btnAppeals);

        // Handle deep-linked tab
        int startTab = getArguments() != null ? getArguments().getInt(ARG_START_TAB, TAB_ITR) : TAB_ITR;

        if (startTab == TAB_AIT) {
            loadSub(new AitListFragment());
            setActive(btnAit, btnItr, btnCertificate, btnBusiness, btnAudits, btnAppeals);
        } else if (startTab == TAB_CERTIFICATE) {
            loadSub(new CertificateFragment());
            setActive(btnCertificate, btnItr, btnAit, btnBusiness, btnAudits, btnAppeals);
        } else if (startTab == TAB_BUSINESS) {
            loadSub(new BusinessFragment());
            setActive(btnBusiness, btnItr, btnAit, btnCertificate, btnAudits, btnAppeals);
        } else if (startTab == TAB_AUDITS) {
            loadSub(new AuditListFragment());
            setActive(btnAudits, btnItr, btnAit, btnCertificate, btnBusiness, btnAppeals);
        } else if (startTab == TAB_APPEALS) {
            loadSub(new AppealListFragment());
            setActive(btnAppeals, btnItr, btnAit, btnCertificate, btnBusiness, btnAudits);
        } else {
            loadSub(new ItrListFragment());
            setActive(btnItr, btnAit, btnCertificate, btnBusiness, btnAudits, btnAppeals);
        }

        btnItr.setOnClickListener(v -> {
            loadSub(new ItrListFragment());
            setActive(btnItr, btnAit, btnCertificate, btnBusiness, btnAudits, btnAppeals);
        });

        btnAit.setOnClickListener(v -> {
            loadSub(new AitListFragment());
            setActive(btnAit, btnItr, btnCertificate, btnBusiness, btnAudits, btnAppeals);
        });

        btnCertificate.setOnClickListener(v -> {
            loadSub(new CertificateFragment());
            setActive(btnCertificate, btnItr, btnAit, btnBusiness, btnAudits, btnAppeals);
        });

        btnBusiness.setOnClickListener(v -> {
            loadSub(new BusinessFragment());
            setActive(btnBusiness, btnItr, btnAit, btnCertificate, btnAudits, btnAppeals);
        });

        btnAudits.setOnClickListener(v -> {
            loadSub(new AuditListFragment());
            setActive(btnAudits, btnItr, btnAit, btnCertificate, btnBusiness, btnAppeals);
        });

        btnAppeals.setOnClickListener(v -> {
            loadSub(new AppealListFragment());
            setActive(btnAppeals, btnItr, btnAit, btnCertificate, btnBusiness, btnAudits);
        });
    }

    private void loadSub(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.taxContent, fragment)
                .commit();
    }

    private void setActive(Button active, Button... others) {
        active.setTextColor(
                requireContext().getResources().getColor(R.color.primary));
        for (Button b : others) {
            b.setTextColor(
                    requireContext().getResources().getColor(R.color.text_secondary));
        }
    }
}
