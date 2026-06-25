package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Business;
import com.vtms.nirapod.data.model.BusinessVatStatus;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BusinessApi {

    // Taxpayer views their businesses with VAT status
    @GET("api/businesses/by-taxpayer/{taxpayerId}/vat-status")
    Call<List<BusinessVatStatus>> getMyBusinesses(
            @Path("taxpayerId") long taxpayerId);

    @GET("api/businesses/{id}")
    Call<Business> getBusinessById(@Path("id") long id);

    @POST("api/businesses")
    Call<Business> registerBusiness(@Body Map<String, Object> body);
}
