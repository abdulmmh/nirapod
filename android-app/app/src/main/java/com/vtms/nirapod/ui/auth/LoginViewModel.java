package com.vtms.nirapod.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AuthResponse;
import com.vtms.nirapod.data.repository.AuthRepository;
import com.vtms.nirapod.util.NetworkResult;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository repository;
    private final MutableLiveData<NetworkResult<AuthResponse>> loginResult
            = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application app) {
        super(app);
        TokenManager tm = new TokenManager(app);
        repository = new AuthRepository(
                RetrofitClient.authApi(tm), tm
        );
    }

    public LiveData<NetworkResult<AuthResponse>> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            loginResult.setValue(
                    NetworkResult.error("Please enter email and password"));
            return;
        }

        loginResult.setValue(NetworkResult.loading());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Response<AuthResponse> response =
                        repository.login(email, password).execute();

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();

                    if (!"TAXPAYER".equals(auth.getRole())) {
                        loginResult.postValue(
                                NetworkResult.error("Only taxpayers can login here"));
                        return;
                    }

                    repository.saveSession(auth);
                    loginResult.postValue(NetworkResult.success(auth));

                } else if (response.code() == 401) {
                    loginResult.postValue(
                            NetworkResult.error("Invalid email or password"));
                } else if (response.code() == 403) {
                    loginResult.postValue(
                            NetworkResult.error("Account is suspended or inactive"));
                } else {
                    loginResult.postValue(
                            NetworkResult.error("Login failed. Please try again"));
                }

            } catch (Exception e) {
                loginResult.postValue(
                        NetworkResult.error("Cannot connect to server"));
            }
        });
    }
}