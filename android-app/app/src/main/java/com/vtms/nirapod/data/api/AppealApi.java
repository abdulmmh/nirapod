package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Appeal;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppealApi {

    @GET("api/my-portal/appeals")
    Call<List<Appeal>> getMyAppeals();

    @GET("api/my-portal/appeals/{id}")
    Call<Appeal> getAppealById(@Path("id") long id);

    @POST("api/my-portal/appeals")
    Call<Appeal> fileAppeal(@Body Appeal request);
}
