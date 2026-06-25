package com.vtms.nirapod.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.vtms.nirapod.databinding.ActivityLoginBinding;
import com.vtms.nirapod.ui.main.MainActivity;
import com.vtms.nirapod.util.NetworkResult;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel        viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(binding.getRoot());

        setupClickListeners();
        observeLoginResult();
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email    = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            viewModel.login(email, password);
        });
    }

    private void observeLoginResult() {
        viewModel.getLoginResult().observe(this, result -> {

            if (result.isLoading()) {
                showLoading(true);
                hideError();
            }

            else if (result.isSuccess()) {
                showLoading(false);
                goToMain();
            }

            else if (result.isError()) {
                showLoading(false);
                showError(result.message);
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
    }

    private void showError(String message) {
        binding.tvError.setText(message);
        binding.tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        binding.tvError.setVisibility(View.GONE);
    }
}