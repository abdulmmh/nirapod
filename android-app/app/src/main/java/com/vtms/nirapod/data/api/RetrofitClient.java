package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.interceptor.AuthInterceptor;
import com.vtms.nirapod.data.local.TokenManager;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.180.254.149:8080/";

    private static Retrofit retrofit;

    public static Retrofit getInstance(TokenManager tokenManager) {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // API interface
    public static AuthApi authApi(TokenManager tm) {
        return getInstance(tm).create(AuthApi.class);
    }

    public static ItrApi itrApi(TokenManager tm) {
        return getInstance(tm).create(ItrApi.class);
    }

    public static AitApi aitApi(TokenManager tm) {
        return getInstance(tm).create(AitApi.class);
    }

    public static NoticeApi noticeApi(TokenManager tm) {
        return getInstance(tm).create(NoticeApi.class);
    }

    public static PaymentApi paymentApi(TokenManager tm) {
        return getInstance(tm).create(PaymentApi.class);
    }

    public static TinApi tinApi(TokenManager tm) {
        return getInstance(tm).create(TinApi.class);
    }

    public static BusinessApi businessApi(TokenManager tm) {
        return getInstance(tm).create(BusinessApi.class);
    }

    public static TaxpayerApi taxpayerApi(TokenManager tm) {
        return getInstance(tm).create(TaxpayerApi.class);
    }

    public static AuditApi auditApi(TokenManager tm) {
        return getInstance(tm).create(AuditApi.class);
    }

    public static AppealApi appealApi(TokenManager tm) {
        return getInstance(tm).create(AppealApi.class);
    }
}