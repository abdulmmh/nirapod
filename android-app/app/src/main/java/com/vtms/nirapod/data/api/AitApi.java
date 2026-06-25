package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.AitRecord;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface AitApi {

    @GET("api/ait-records/my-records")
    Call<List<AitRecord>> getMyAitRecords();

    @GET("api/ait-records/{id}")
    Call<AitRecord> getMyAitRecordById(@Path("id") long id);

    @POST("api/ait-records")
    Call<AitRecord> createAit(@Body AitRecord record);
}
