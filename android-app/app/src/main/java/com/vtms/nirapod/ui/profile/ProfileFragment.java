package com.vtms.nirapod.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Taxpayer;
import com.vtms.nirapod.ui.auth.LoginActivity;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private Taxpayer profile;
    private TokenManager    tm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tm = new TokenManager(requireContext());

        getParentFragmentManager().setFragmentResultListener("profile_updated", getViewLifecycleOwner(), (requestKey, result) -> loadProfile(view));

        loadProfile(view);

        MaterialButton btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            tm.clearSession();
            startActivity(new Intent(requireContext(), LoginActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        });
    }

    private void loadProfile(View view) {
        TextView tvAvatar    = view.findViewById(R.id.tvAvatar);
        TextView tvName      = view.findViewById(R.id.tvProfileName);
        TextView tvTin       = view.findViewById(R.id.tvProfileTin);
        ProgressBar pb       = view.findViewById(R.id.progressBar);
        CardView cardPersonal= view.findViewById(R.id.cardPersonal);
        CardView cardAddress = view.findViewById(R.id.cardAddress);
        MaterialButton btnEdit   = view.findViewById(R.id.btnEditProfile);
        ImageView ivProfile = view.findViewById(R.id.ivProfile);

        // Instant data from token
        String name = tm.getUserName();
        if (name != null && !name.isEmpty()) {
            tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
        tvName.setText(name);
        tvTin.setText(tm.getTinNumber().isEmpty() ? "---" : tm.getTinNumber());

        pb.setVisibility(View.VISIBLE);

        // Load full profile from API
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<Taxpayer> response =
                        RetrofitClient.taxpayerApi(tm).getMyProfile().execute();

                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pb.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            profile = response.body();
                            populateView(view, profile);
                        }
                        cardPersonal.setVisibility(View.VISIBLE);
                        cardAddress.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pb.setVisibility(View.GONE);
                        cardPersonal.setVisibility(View.VISIBLE);
                        cardAddress.setVisibility(View.VISIBLE);
                    });
                }
            }
        });

        btnEdit.setOnClickListener(v -> {
            if (profile != null) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.navHostFragment,
                                EditProfileFragment.newInstance(profile))
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(requireContext(),
                        "Profile loading, please wait",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateView(View view, Taxpayer p) {
        String name = p.getDisplayName();

        TextView tvAvatar = view.findViewById(R.id.tvAvatar);
        TextView tvName   = view.findViewById(R.id.tvProfileName);
        TextView tvTin    = view.findViewById(R.id.tvProfileTin);
        TextView tvStatus = view.findViewById(R.id.tvProfileStatus);
        ImageView ivProfile = view.findViewById(R.id.ivProfile);

        if (!name.isEmpty()) {
            tvAvatar.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
        tvName.setText(name);
        tvTin.setText(p.getTinNumber() != null ? p.getTinNumber() : "---");
        tvStatus.setText(p.getApprovalStatus() != null
                ? p.getApprovalStatus() : "---");

        if (p.getPhotoPath() != null && ivProfile != null) {
            String url = "http://10.0.2.2:8080" + p.getPhotoPath();
            Glide.with(this).load(url).circleCrop().into(ivProfile);
            tvAvatar.setVisibility(View.GONE);
            ivProfile.setVisibility(View.VISIBLE);
        } else if (ivProfile != null) {
            tvAvatar.setVisibility(View.VISIBLE);
            ivProfile.setVisibility(View.GONE);
        }

        set(view, R.id.tvEmail,
                p.getEmail() != null ? p.getEmail() : "---");
        set(view, R.id.tvPhone,
                p.getPhone() != null ? p.getPhone() : "Not provided");
        set(view, R.id.tvGender,
                p.getGender() != null ? p.getGender() : "Not provided");
        set(view, R.id.tvProfession,
                p.getProfession() != null ? p.getProfession() : "Not provided");

        // Build address string
        String addr = buildAddress(p.getPresentAddress());
        set(view, R.id.tvAddress, addr);
    }

    private String buildAddress(Taxpayer.AddressInfo a) {
        if (a == null) return "Not provided";
        StringBuilder sb = new StringBuilder();
        if (a.getRoadVillage() != null) sb.append(a.getRoadVillage());
        if (a.getThana() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(a.getThana());
        }
        if (a.getDistrict() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(a.getDistrict());
        }
        if (a.getDivision() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(a.getDivision());
        }
        return sb.length() > 0 ? sb.toString() : "Not provided";
    }

    private void set(View parent, int id, String text) {
        TextView tv = parent.findViewById(id);
        if (tv != null) tv.setText(text);
    }
}
