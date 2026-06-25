package com.vtms.nirapod.ui.payment;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;

public class PayHistoryFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnHistory     = view.findViewById(R.id.btnHistory);
        Button btnMakePayment = view.findViewById(R.id.btnMakePayment);

        loadSub(new PaymentListFragment());

        btnHistory.setOnClickListener(v -> {
            loadSub(new PaymentListFragment());
            btnHistory.setAlpha(1f);
            btnMakePayment.setAlpha(0.6f);
        });

        btnMakePayment.setOnClickListener(v -> {
            loadSub(new MakePaymentFragment());
            btnMakePayment.setAlpha(1f);
            btnHistory.setAlpha(0.6f);
        });
    }

    private void loadSub(Fragment f) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.paymentContent, f)
                .commit();
    }
}