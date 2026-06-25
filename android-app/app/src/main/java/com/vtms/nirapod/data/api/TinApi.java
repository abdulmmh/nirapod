package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.TinRecord;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface TinApi {

    @GET("api/tins")
    Call<List<TinRecord>> getMyTins();

    @Streaming
    @GET("api/tins/{id}/certificate")
    Call<ResponseBody> downloadCertificate(@Path("id") long id);
}