package com.vtms.nirapod.ui.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Taxpayer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private Taxpayer profile;
    private EditText etFullName, etNid, etFathersName, etMothersName, etDob, etEmail, etPhone, etProfession;
    private EditText etDivision, etDistrict, etThana, etRoadVillage;
    private ImageView ivProfilePhoto;
    private ProgressBar pbLoading;
    private TextView tvError;
    private Uri selectedPhotoUri;

    private final ActivityResultLauncher<Intent> photoPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedPhotoUri = result.getData().getData();
                    ivProfilePhoto.setImageURI(selectedPhotoUri);
                }
            }
    );

    public static EditProfileFragment newInstance(Taxpayer p) {
        EditProfileFragment f = new EditProfileFragment();
        f.profile = p;
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        prefillData();
        setupListeners(view);
    }

    private void bindViews(View v) {
        etFullName    = v.findViewById(R.id.etFullName);
        etNid         = v.findViewById(R.id.etNid);
        etFathersName = v.findViewById(R.id.etFathersName);
        etMothersName = v.findViewById(R.id.etMothersName);
        etDob         = v.findViewById(R.id.etDob);
        etEmail       = v.findViewById(R.id.etEmail);
        etPhone       = v.findViewById(R.id.etPhone);
        etProfession  = v.findViewById(R.id.etProfession);
        etDivision    = v.findViewById(R.id.etDivision);
        etDistrict    = v.findViewById(R.id.etDistrict);
        etThana       = v.findViewById(R.id.etThana);
        etRoadVillage = v.findViewById(R.id.etRoadVillage);
        ivProfilePhoto = v.findViewById(R.id.ivProfilePhoto);
        pbLoading     = v.findViewById(R.id.pb_loading);
        tvError       = v.findViewById(R.id.tvSaveError);
    }

    private void prefillData() {
        if (profile == null) return;

        etFullName.setText(profile.getFullName());
        etNid.setText(profile.getNid());
        etFathersName.setText(profile.getFathersName());
        etMothersName.setText(profile.getMothersName());
        etDob.setText(profile.getDateOfBirth());
        etEmail.setText(profile.getEmail());
        etPhone.setText(profile.getPhone());
        etProfession.setText(profile.getProfession());

        Taxpayer.AddressInfo addr = profile.getPresentAddress();
        if (addr != null) {
            etDivision.setText(addr.getDivision());
            etDistrict.setText(addr.getDistrict());
            etThana.setText(addr.getThana());
            etRoadVillage.setText(addr.getRoadVillage());
        }

        if (profile.getPhotoPath() != null) {
            String url = "http://10.0.2.2:8080" + profile.getPhotoPath();
            Glide.with(this).load(url).circleCrop().into(ivProfilePhoto);
        }
    }

    private void setupListeners(View v) {
        v.findViewById(R.id.btnBack).setOnClickListener(v1 -> 
                requireActivity().getSupportFragmentManager().popBackStack());
        
        v.findViewById(R.id.btnCancel).setOnClickListener(v1 -> 
                requireActivity().getSupportFragmentManager().popBackStack());

        v.findViewById(R.id.btnChangePhoto).setOnClickListener(v1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerLauncher.launch(intent);
        });

        etDob.setOnClickListener(v1 -> showDatePicker());

        v.findViewById(R.id.btnSave).setOnClickListener(v1 -> saveProfile());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, day) -> {
            String date = String.format(Locale.US, "%d-%02d-%02d", year, month + 1, day);
            etDob.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveProfile() {
        if (profile == null) return;
        
        tvError.setVisibility(View.GONE);
        pbLoading.setVisibility(View.VISIBLE);

        // 1. Update text fields
        profile.setFullName(etFullName.getText().toString());
        profile.setNid(etNid.getText().toString());
        profile.setFathersName(etFathersName.getText().toString());
        profile.setMothersName(etMothersName.getText().toString());
        profile.setDateOfBirth(etDob.getText().toString());
        profile.setEmail(etEmail.getText().toString());
        profile.setPhone(etPhone.getText().toString());
        profile.setProfession(etProfession.getText().toString());

        Taxpayer.AddressInfo addr = profile.getPresentAddress();
        if (addr == null) {
            addr = new Taxpayer.AddressInfo();
            profile.setPresentAddress(addr);
        }
        addr.setDivision(etDivision.getText().toString());
        addr.setDistrict(etDistrict.getText().toString());
        addr.setThana(etThana.getText().toString());
        addr.setRoadVillage(etRoadVillage.getText().toString());

        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // First upload photo if selected
                if (selectedPhotoUri != null) {
                    uploadPhotoSync(tm);
                }

                // Then update profile
                Response<Taxpayer> response = RetrofitClient.taxpayerApi(tm)
                        .updateProfile(profile.getId(), profile).execute();

                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().setFragmentResult("profile_updated", new Bundle());
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        tvError.setText("Update failed");
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pbLoading.setVisibility(View.GONE);
                    tvError.setText(String.format("Error: %s", e.getMessage()));
                    tvError.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void uploadPhotoSync(TokenManager tm) throws Exception {
        File file = getFileFromUri(selectedPhotoUri);
        if (file == null) return;

        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        Response<Taxpayer> resp = RetrofitClient.taxpayerApi(tm)
                .uploadPhoto(profile.getId(), body).execute();
        
        if (resp.isSuccessful() && resp.body() != null) {
            profile.setPhotoPath(resp.body().getPhotoPath());
        }
    }

    private File getFileFromUri(Uri uri) {
        try {
            InputStream is = requireContext().getContentResolver().openInputStream(uri);
            if (is == null) return null;
            File file = new File(requireContext().getCacheDir(), "upload_photo.jpg");
            FileOutputStream os = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            os.close();
            is.close();
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
