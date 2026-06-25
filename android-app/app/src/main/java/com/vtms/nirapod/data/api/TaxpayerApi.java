package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Taxpayer;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface TaxpayerApi {

    // Search by TIN or name
    @GET("api/taxpayers")
    Call<List<Taxpayer>> search(@Query("search") String query);

    @GET("api/taxpayers/{id}")
    Call<Taxpayer> getById(@Path("id") long id);

    @GET("api/taxpayers/me")
    Call<Taxpayer> getMyProfile();


    @PUT("api/taxpayers/{id}")
    Call<Taxpayer> updateProfile(
            @Path("id") long id,
            @Body Taxpayer profile);

    @Multipart
    @POST("api/taxpayers/{id}/upload-photo")
    Call<Taxpayer> uploadPhoto(
            @Path("id") long id,
            @Part MultipartBody.Part photo);
}
