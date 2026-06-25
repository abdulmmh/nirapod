package com.vtms.nirapod.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.ui.home.HomeFragment;
import com.vtms.nirapod.ui.notice.NoticeListFragment;
import com.vtms.nirapod.ui.payment.PayHistoryFragment;
import com.vtms.nirapod.ui.profile.ProfileFragment;
import com.vtms.nirapod.ui.tax.TaxFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, new HomeFragment())
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            // Clear back stack when switching main tabs
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_tax) {
                fragment = new TaxFragment();
            } else if (id == R.id.nav_payment) {
                fragment = new PayHistoryFragment();
            } else if (id == R.id.nav_notice) {
                fragment = new NoticeListFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                fragment = new HomeFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navHostFragment, fragment)
                    .commit();
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        // If we are at a detail page (backstack not empty), go back normally
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (bottomNav.getSelectedItemId() != R.id.nav_home) {
            // If we are on another main tab but not Home, go back to Home
            bottomNav.setSelectedItemId(R.id.nav_home);
        } else {
            // Otherwise, exit app
            finish();
        }
    }
}
