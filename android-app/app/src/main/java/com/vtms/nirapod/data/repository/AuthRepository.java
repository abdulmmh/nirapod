package com.vtms.nirapod.data.repository;

import com.vtms.nirapod.data.api.AuthApi;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AuthResponse;
import com.vtms.nirapod.data.model.LoginRequest;
import retrofit2.Call;

public class AuthRepository {

    private final AuthApi     authApi;
    private final TokenManager tokenManager;

    public AuthRepository(AuthApi authApi, TokenManager tokenManager) {
        this.authApi      = authApi;
        this.tokenManager = tokenManager;
    }

    public Call<AuthResponse> login(String email, String password) {
        return authApi.login(new LoginRequest(email, password));
    }

    public Call<AuthResponse> getProfile() {
        return authApi.getProfile();
    }

    public void saveSession(AuthResponse auth) {
        tokenManager.saveSession(
                auth.getToken(),
                auth.getFullName(),
                auth.getEmail(),
                auth.getRole(),
                auth.getTinNumber(),
                auth.getTaxpayerId()
        );
    }

    public Call<Void> logout() {
        return authApi.logout();
    }
}