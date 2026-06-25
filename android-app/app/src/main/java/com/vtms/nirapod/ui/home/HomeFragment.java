package com.vtms.nirapod.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.ui.tax.TaxFragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TokenManager tm = new TokenManager(requireContext());

        TextView tvName   = view.findViewById(R.id.tvName);
        TextView tvTin    = view.findViewById(R.id.tvTin);
        TextView tvStatus = view.findViewById(R.id.tvStatus);

        tvName.setText(tm.getUserName());
        tvTin.setText(tm.getTinNumber().isEmpty()
                ? "Not assigned" : tm.getTinNumber());
        tvStatus.setText("APPROVED");

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottomNav);

        view.findViewById(R.id.cardItr).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_tax);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            TaxFragment.newInstance(TaxFragment.TAB_ITR))
                    .commit();
        });

        view.findViewById(R.id.cardAit).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_tax);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            TaxFragment.newInstance(TaxFragment.TAB_AIT))
                    .commit();
        });

        view.findViewById(R.id.cardNotice).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_notice);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            new com.vtms.nirapod.ui.notice.NoticeListFragment())
                    .commit();
        });

        view.findViewById(R.id.cardTin).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_tax);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            TaxFragment.newInstance(TaxFragment.TAB_CERTIFICATE))
                    .commit();
        });

        view.findViewById(R.id.cardBusiness).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_tax);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            TaxFragment.newInstance(TaxFragment.TAB_BUSINESS))
                    .commit();
        });

        view.findViewById(R.id.cardAudit).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_tax);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            TaxFragment.newInstance(TaxFragment.TAB_AUDITS))
                    .commit();
        });

        view.findViewById(R.id.cardAppeal).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_tax);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            TaxFragment.newInstance(TaxFragment.TAB_APPEALS))
                    .commit();
        });

        view.findViewById(R.id.cardPayment).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_payment);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            new com.vtms.nirapod.ui.payment.PayHistoryFragment())
                    .commit();
        });
    }
}
