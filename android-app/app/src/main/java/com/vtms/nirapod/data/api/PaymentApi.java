package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.OutstandingItem;
import com.vtms.nirapod.data.model.Payment;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PaymentApi {

    @GET("api/payments")
    Call<List<Payment>> getMyPayments();

    @POST("api/payments")
    Call<Payment> createPayment(@Body Payment payment);

    @GET("api/payments/outstanding")
    Call<List<OutstandingItem>> getOutstanding(@Query("taxpayerId") long taxpayerId);
}