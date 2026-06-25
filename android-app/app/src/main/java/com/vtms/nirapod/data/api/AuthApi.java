package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.AuthResponse;
import com.vtms.nirapod.data.model.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("api/auth/profile")
    Call<AuthResponse> getProfile();

    @POST("api/auth/logout")
    Call<Void> logout();
}