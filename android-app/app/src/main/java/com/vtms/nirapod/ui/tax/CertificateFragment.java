package com.vtms.nirapod.ui.tax;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.TinRecord;
import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class CertificateFragment extends Fragment {

    private TokenManager tm;
    private TinRecord    myTin;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ScrollView scroll = new ScrollView(requireContext());
        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.background));
        scroll.addView(root);

        tm = new TokenManager(requireContext());

        // Header
        TextView header = new TextView(requireContext());
        header.setText("TIN Certificate");
        header.setTextSize(20f);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setTextColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.text_primary));
        root.addView(header);

        // Loading
        ProgressBar pb = new ProgressBar(requireContext());
        LinearLayout.LayoutParams pbp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pbp.setMargins(0, 48, 0, 0);
        pb.setLayoutParams(pbp);
        root.addView(pb);

        // Info card
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.setVisibility(View.GONE);
        root.addView(infoLayout);

        // Download button
        MaterialButton btnDownload = new MaterialButton(requireContext());
        btnDownload.setText("Download TIN Certificate (PDF)");
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bp.setMargins(0, 32, 0, 0);
        btnDownload.setLayoutParams(bp);
        btnDownload.setVisibility(View.GONE);
        root.addView(btnDownload);

        TextView tvResult = new TextView(requireContext());
        tvResult.setVisibility(View.GONE);
        tvResult.setTextSize(13f);
        tvResult.setPadding(0, 16, 0, 0);
        root.addView(tvResult);

        // Load TIN data
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<TinRecord>> response =
                        RetrofitClient.tinApi(tm).getMyTins().execute();

                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pb.setVisibility(View.GONE);

                        if (response.isSuccessful()
                                && response.body() != null
                                && !response.body().isEmpty()) {

                            myTin = response.body().get(0);
                            showTinInfo(infoLayout, myTin);
                            infoLayout.setVisibility(View.VISIBLE);
                            btnDownload.setVisibility(View.VISIBLE);

                        } else {
                            TextView tvErr = new TextView(requireContext());
                            tvErr.setText("No TIN certificate found");
                            tvErr.setTextSize(15f);
                            tvErr.setTextColor(requireContext().getResources()
                                    .getColor(com.vtms.nirapod.R.color.text_secondary));
                            tvErr.setPadding(0, 48, 0, 0);
                            root.addView(tvErr);
                        }
                    });
                }
            } catch (Exception e) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        pb.setVisibility(View.GONE);
                        TextView tvErr = new TextView(requireContext());
                        tvErr.setText("Cannot connect to server");
                        tvErr.setTextSize(15f);
                        tvErr.setTextColor(requireContext().getResources()
                                .getColor(com.vtms.nirapod.R.color.error));
                        root.addView(tvErr);
                    });
                }
            }
        });

        btnDownload.setOnClickListener(v -> {
            if (myTin == null) return;
            btnDownload.setEnabled(false);
            btnDownload.setText("Downloading...");
            tvResult.setVisibility(View.GONE);
            downloadPdf(myTin.getId(), btnDownload, tvResult);
        });

        return scroll;
    }

    private void showTinInfo(LinearLayout layout, TinRecord tin) {
        layout.removeAllViews();
        addInfoRow(layout, "TIN Number",  tin.getTinNumber());
        addInfoRow(layout, "Tax Zone",    tin.getTaxZone());
        addInfoRow(layout, "Tax Circle",  tin.getTaxCircle());
        addInfoRow(layout, "Issued Date", tin.getIssuedDate() != null
                && tin.getIssuedDate().length() >= 10
                ? tin.getIssuedDate().substring(0, 10) : "---");
    }

    private void addInfoRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 24, 0, 0);
        row.setLayoutParams(lp);

        TextView lbl = new TextView(requireContext());
        lbl.setText(label);
        lbl.setTextSize(13f);
        lbl.setTextColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.text_secondary));
        lbl.setLayoutParams(new LinearLayout.LayoutParams(280,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        row.addView(lbl);

        TextView val = new TextView(requireContext());
        val.setText(value != null ? value : "---");
        val.setTextSize(14f);
        val.setTypeface(null, android.graphics.Typeface.BOLD);
        val.setTextColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.text_primary));
        row.addView(val);

        parent.addView(row);
    }

    private void downloadPdf(long tinId,
                             MaterialButton btnDownload,
                             TextView tvResult) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<okhttp3.ResponseBody> response =
                        RetrofitClient.tinApi(tm)
                                .downloadCertificate(tinId).execute();

                if (response.isSuccessful() && response.body() != null) {
                    // Save to cache directory
                    File pdfFile = new File(
                            requireContext().getCacheDir(),
                            "TIN_Certificate_" + tinId + ".pdf");

                    try (InputStream is = response.body().byteStream();
                         OutputStream os = new FileOutputStream(pdfFile)) {
                        byte[] buf = new byte[4096];
                        int len;
                        while ((len = is.read(buf)) != -1) {
                            os.write(buf, 0, len);
                        }
                    }

                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(() -> {
                            if (!isAdded()) return;
                            btnDownload.setEnabled(true);
                            btnDownload.setText("Download TIN Certificate (PDF)");
                            openPdf(pdfFile, tvResult);
                        });
                    }
                } else {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(() -> {
                            if (!isAdded()) return;
                            btnDownload.setEnabled(true);
                            btnDownload.setText("Download TIN Certificate (PDF)");
                            tvResult.setText("Download failed. Try again.");
                            tvResult.setTextColor(requireContext().getResources()
                                    .getColor(com.vtms.nirapod.R.color.error));
                            tvResult.setVisibility(View.VISIBLE);
                        });
                    }
                }
            } catch (Exception e) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        if (!isAdded()) return;
                        btnDownload.setEnabled(true);
                        btnDownload.setText("Download TIN Certificate (PDF)");
                        tvResult.setText("Error: " + e.getMessage());
                        tvResult.setTextColor(requireContext().getResources()
                                .getColor(com.vtms.nirapod.R.color.error));
                        tvResult.setVisibility(View.VISIBLE);
                    });
                }
            }
        });
    }

    private void openPdf(File file, TextView tvResult) {
        try {
            android.net.Uri uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            tvResult.setText("PDF saved. Install a PDF viewer to open it.");
            tvResult.setTextColor(requireContext().getResources()
                    .getColor(com.vtms.nirapod.R.color.warning));
            tvResult.setVisibility(View.VISIBLE);
        }
    }
}