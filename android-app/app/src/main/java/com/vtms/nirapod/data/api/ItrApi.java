package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.ItrRecord;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ItrApi {

    @GET("api/income-tax-returns/my-returns")
    Call<List<ItrRecord>> getMyReturns();

    @GET("api/income-tax-returns/{id}")
    Call<ItrRecord> getReturnById(@Path("id") long id);
}