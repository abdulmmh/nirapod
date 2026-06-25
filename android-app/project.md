This file is a merged representation of a subset of the codebase, containing specifically included files, combined into a single document by Repomix.

<file_summary>
This section contains a summary of this file.

<purpose>
This file contains a packed representation of a subset of the repository's contents that is considered the most important context.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.
</purpose>

<file_format>
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  - File path as an attribute
  - Full contents of the file
</file_format>

<usage_guidelines>
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.
</usage_guidelines>

<notes>
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Only files matching these patterns are included: **/*.java, **/*.kt, **/*.xml
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Files are sorted by Git change count (files with more changes are at the bottom)
</notes>

</file_summary>

<directory_structure>
app/src/androidTest/java/com/vtms/nirapod/ExampleInstrumentedTest.java
app/src/main/AndroidManifest.xml
app/src/main/java/com/vtms/nirapod/data/api/AitApi.java
app/src/main/java/com/vtms/nirapod/data/api/AuthApi.java
app/src/main/java/com/vtms/nirapod/data/api/BusinessApi.java
app/src/main/java/com/vtms/nirapod/data/api/ItrApi.java
app/src/main/java/com/vtms/nirapod/data/api/NoticeApi.java
app/src/main/java/com/vtms/nirapod/data/api/PaymentApi.java
app/src/main/java/com/vtms/nirapod/data/api/RetrofitClient.java
app/src/main/java/com/vtms/nirapod/data/api/TinApi.java
app/src/main/java/com/vtms/nirapod/data/interceptor/AuthInterceptor.java
app/src/main/java/com/vtms/nirapod/data/local/TokenManager.java
app/src/main/java/com/vtms/nirapod/data/model/AitRecord.java
app/src/main/java/com/vtms/nirapod/data/model/AuthResponse.java
app/src/main/java/com/vtms/nirapod/data/model/Business.java
app/src/main/java/com/vtms/nirapod/data/model/ItrRecord.java
app/src/main/java/com/vtms/nirapod/data/model/LoginRequest.java
app/src/main/java/com/vtms/nirapod/data/model/Notice.java
app/src/main/java/com/vtms/nirapod/data/model/NoticeReplyRequest.java
app/src/main/java/com/vtms/nirapod/data/model/Payment.java
app/src/main/java/com/vtms/nirapod/data/model/PaymentRequest.java
app/src/main/java/com/vtms/nirapod/data/model/TinRecord.java
app/src/main/java/com/vtms/nirapod/data/repository/AuthRepository.java
app/src/main/java/com/vtms/nirapod/data/repository/ItrRepository.java
app/src/main/java/com/vtms/nirapod/ui/auth/LoginActivity.java
app/src/main/java/com/vtms/nirapod/ui/auth/LoginViewModel.java
app/src/main/java/com/vtms/nirapod/ui/auth/SplashActivity.java
app/src/main/java/com/vtms/nirapod/ui/home/HomeFragment.java
app/src/main/java/com/vtms/nirapod/ui/main/MainActivity.java
app/src/main/java/com/vtms/nirapod/ui/notice/NoticeAdapter.java
app/src/main/java/com/vtms/nirapod/ui/notice/NoticeDetailFragment.java
app/src/main/java/com/vtms/nirapod/ui/notice/NoticeListFragment.java
app/src/main/java/com/vtms/nirapod/ui/payment/MakePaymentFragment.java
app/src/main/java/com/vtms/nirapod/ui/payment/PayHistoryFragment.java
app/src/main/java/com/vtms/nirapod/ui/payment/PaymentAdapter.java
app/src/main/java/com/vtms/nirapod/ui/payment/PaymentListFragment.java
app/src/main/java/com/vtms/nirapod/ui/profile/ProfileFragment.java
app/src/main/java/com/vtms/nirapod/ui/tax/AitAdapter.java
app/src/main/java/com/vtms/nirapod/ui/tax/AitListFragment.java
app/src/main/java/com/vtms/nirapod/ui/tax/BusinessFragment.java
app/src/main/java/com/vtms/nirapod/ui/tax/CertificateFragment.java
app/src/main/java/com/vtms/nirapod/ui/tax/ItrAdapter.java
app/src/main/java/com/vtms/nirapod/ui/tax/ItrListFragment.java
app/src/main/java/com/vtms/nirapod/ui/tax/ItrViewModel.java
app/src/main/java/com/vtms/nirapod/ui/tax/TaxFragment.java
app/src/main/java/com/vtms/nirapod/util/NetworkResult.java
app/src/main/res/drawable/ic_launcher_background.xml
app/src/main/res/drawable/ic_launcher_foreground.xml
app/src/main/res/drawable/status_badge_bg.xml
app/src/main/res/layout/activity_login.xml
app/src/main/res/layout/activity_main.xml
app/src/main/res/layout/fragment_home.xml
app/src/main/res/layout/fragment_itr_list.xml
app/src/main/res/layout/fragment_make_payment.xml
app/src/main/res/layout/fragment_notice_list.xml
app/src/main/res/layout/fragment_payment.xml
app/src/main/res/layout/fragment_profile.xml
app/src/main/res/layout/fragment_tax.xml
app/src/main/res/layout/item_ait.xml
app/src/main/res/layout/item_itr.xml
app/src/main/res/layout/item_notice.xml
app/src/main/res/layout/item_payment.xml
app/src/main/res/menu/bottom_nav_menu.xml
app/src/main/res/mipmap-anydpi/ic_launcher_round.xml
app/src/main/res/mipmap-anydpi/ic_launcher.xml
app/src/main/res/values-night/themes.xml
app/src/main/res/values/colors.xml
app/src/main/res/values/strings.xml
app/src/main/res/values/themes.xml
app/src/main/res/xml/backup_rules.xml
app/src/main/res/xml/data_extraction_rules.xml
app/src/main/res/xml/file_paths.xml
app/src/test/java/com/vtms/nirapod/ExampleUnitTest.java
</directory_structure>

<files>
This section contains the contents of the repository's files.

<file path="app/src/androidTest/java/com/vtms/nirapod/ExampleInstrumentedTest.java">
package com.vtms.nirapod;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.vtms.nirapod", appContext.getPackageName());
    }
}
</file>

<file path="app/src/main/AndroidManifest.xml">
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Nirapod"
        android:usesCleartextTraffic="true">

        <!-- LAUNCHER = SplashActivity -->
        <activity
            android:name=".ui.auth.SplashActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ui.auth.LoginActivity"
            android:exported="false"/>

        <activity
            android:name=".ui.main.MainActivity"
            android:exported="false"/>

        <provider
            android:name="androidx.core.content.FileProvider"
            android:authorities="${applicationId}.provider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
        </provider>

    </application>

</manifest>
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/AitApi.java">
package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.AitRecord;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AitApi {

    @GET("api/ait-records/my-records")
    Call<List<AitRecord>> getMyAitRecords();

    @GET("api/ait-records/{id}")
    Call<AitRecord> getAitById(@Path("id") long id);
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/AuthApi.java">
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
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/BusinessApi.java">
package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Business;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface BusinessApi {

    @GET("api/businesses")
    Call<List<Business>> getMyBusinesses();
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/ItrApi.java">
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
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/NoticeApi.java">
package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Notice;
import com.vtms.nirapod.data.model.NoticeReplyRequest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface NoticeApi {

    @GET("api/notices")
    Call<List<Notice>> getMyNotices();

    @GET("api/notices/{id}")
    Call<Notice> getNoticeById(@Path("id") long id);

    @PATCH("api/notices/{id}/read")
    Call<Void> markAsRead(@Path("id") long id);

    @PATCH("api/notices/{id}/respond")
    Call<Notice> replyToNotice(
            @Path("id") long id,
            @Body NoticeReplyRequest request
    );
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/PaymentApi.java">
package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Payment;
import com.vtms.nirapod.data.model.PaymentRequest;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PaymentApi {

    @GET("api/payments")
    Call<List<Payment>> getMyPayments();

    @POST("api/payments")
    Call<Payment> makePayment(@Body PaymentRequest request);
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/RetrofitClient.java">
package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.interceptor.AuthInterceptor;
import com.vtms.nirapod.data.local.TokenManager;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // Emulator থেকে localhost = 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2:8080/";

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

    // API interface গুলো এখান থেকে নেবে
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
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/api/TinApi.java">
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
    @GET("api/tins/{id}/download-certificate")
    Call<ResponseBody> downloadCertificate(@Path("id") long id);
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/interceptor/AuthInterceptor.java">
package com.vtms.nirapod.data.interceptor;

import com.vtms.nirapod.data.local.TokenManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = tokenManager.getToken();

        Request request = chain.request();

        if (token != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .build();
        }

        Response response = chain.proceed(request);

        if (response.code() == 401) {
            tokenManager.clearSession();
        }

        return response;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/local/TokenManager.java">
package com.vtms.nirapod.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class TokenManager {

    private static final String PREF_NAME  = "nirapod_secure";
    private static final String KEY_TOKEN  = "jwt_token";
    private static final String KEY_NAME   = "user_name";
    private static final String KEY_EMAIL  = "user_email";
    private static final String KEY_ROLE   = "user_role";
    private static final String KEY_TIN    = "tin_number";
    private static final String KEY_TP_ID  = "taxpayer_id";

    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            throw new RuntimeException("TokenManager init failed: " + e.getMessage());
        }
    }

    public void saveSession(String token, String name,
                            String email, String role,
                            String tin, long taxpayerId) {
        prefs.edit()
                .putString(KEY_TOKEN,  token)
                .putString(KEY_NAME,   name)
                .putString(KEY_EMAIL,  email)
                .putString(KEY_ROLE,   role)
                .putString(KEY_TIN,    tin)
                .putLong  (KEY_TP_ID,  taxpayerId)
                .apply();
    }

    public String  getToken()      { return prefs.getString(KEY_TOKEN, null); }
    public String  getUserName()   { return prefs.getString(KEY_NAME,  ""); }
    public String  getUserEmail()  { return prefs.getString(KEY_EMAIL, ""); }
    public String  getUserRole()   { return prefs.getString(KEY_ROLE,  ""); }
    public String  getTinNumber()  { return prefs.getString(KEY_TIN,   ""); }
    public long    getTaxpayerId() { return prefs.getLong  (KEY_TP_ID, -1); }
    public boolean isLoggedIn()    { return getToken() != null; }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/AitRecord.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class AitRecord {

    @SerializedName("id")
    private long id;

    @SerializedName("aitNumber")
    private String aitNumber;

    @SerializedName("status")
    private String status;

    @SerializedName("challanNumber")
    private String challanNumber;

    @SerializedName("bankName")
    private String bankName;

    @SerializedName("depositDate")
    private String depositDate;

    @SerializedName("aitAmount")
    private double aitAmount;

    @SerializedName("sourceOfIncome")
    private String sourceOfIncome;

    @SerializedName("assignedOfficerName")
    private String assignedOfficerName;

    public long   getId()                  { return id; }
    public String getAitNumber()           { return aitNumber; }
    public String getStatus()              { return status; }
    public String getChallanNumber()       { return challanNumber; }
    public String getBankName()            { return bankName; }
    public String getDepositDate()         { return depositDate; }
    public double getAitAmount()           { return aitAmount; }
    public String getSourceOfIncome()      { return sourceOfIncome; }
    public String getAssignedOfficerName() { return assignedOfficerName; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/AuthResponse.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("id")
    private long id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("role")
    private String role;

    @SerializedName("token")
    private String token;

    @SerializedName("taxpayerId")
    private long taxpayerId;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("approvalStatus")
    private String approvalStatus;

    @SerializedName("photoUrl")
    private String photoUrl;

    // Getters
    public long   getId()             { return id; }
    public String getFullName()       { return fullName; }
    public String getEmail()          { return email; }
    public String getRole()           { return role; }
    public String getToken()          { return token; }
    public long   getTaxpayerId()     { return taxpayerId; }
    public String getTinNumber()      { return tinNumber; }
    public String getApprovalStatus() { return approvalStatus; }
    public String getPhotoUrl()       { return photoUrl; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/Business.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class Business {

    @SerializedName("id")
    private long id;

    @SerializedName("businessName")
    private String businessName;

    @SerializedName("businessType")
    private String businessType;

    @SerializedName("status")
    private String status;

    @SerializedName("registrationNumber")
    private String registrationNumber;

    public long   getId()                  { return id; }
    public String getBusinessName()        { return businessName; }
    public String getBusinessType()        { return businessType; }
    public String getStatus()              { return status; }
    public String getRegistrationNumber()  { return registrationNumber; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/ItrRecord.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class ItrRecord {

    @SerializedName("id")
    private long id;

    @SerializedName("assessmentYear")
    private String assessmentYear;

    @SerializedName("status")
    private String status;

    @SerializedName("totalIncome")
    private double totalIncome;

    @SerializedName("taxableIncome")
    private double taxableIncome;

    @SerializedName("grossTax")
    private double grossTax;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("submissionDate")
    private String submissionDate;

    @SerializedName("acknowledgementNo")
    private String acknowledgementNo;

    public long   getId()                { return id; }
    public String getAssessmentYear()    { return assessmentYear; }
    public String getStatus()            { return status; }
    public double getTotalIncome()       { return totalIncome; }
    public double getTaxableIncome()     { return taxableIncome; }
    public double getGrossTax()          { return grossTax; }
    public String getTinNumber()         { return tinNumber; }
    public String getSubmissionDate()    { return submissionDate; }
    public String getAcknowledgementNo() { return acknowledgementNo; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/LoginRequest.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public LoginRequest(String email, String password) {
        this.email    = email;
        this.password = password;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/Notice.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class Notice {

    @SerializedName("id")
    private long id;

    @SerializedName("subject")
    private String subject;

    @SerializedName("body")
    private String body;

    @SerializedName("noticeType")
    private String noticeType;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("responseText")
    private String responseText;

    public long   getId()           { return id; }
    public String getSubject()      { return subject; }
    public String getBody()         { return body; }
    public String getNoticeType()   { return noticeType; }
    public boolean isRead()         { return isRead; }
    public String getCreatedAt()    { return createdAt; }
    public String getResponseText() { return responseText; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/NoticeReplyRequest.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class NoticeReplyRequest {

    @SerializedName("responseText")
    private String responseText;

    public NoticeReplyRequest(String responseText) {
        this.responseText = responseText;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/Payment.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("id")
    private long id;

    @SerializedName("amount")
    private double amount;

    @SerializedName("status")
    private String status;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("transactionId")
    private String transactionId;

    @SerializedName("paymentDate")
    private String paymentDate;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    public long   getId()            { return id; }
    public double getAmount()        { return amount; }
    public String getStatus()        { return status; }
    public String getPaymentType()   { return paymentType; }
    public String getTransactionId() { return transactionId; }
    public String getPaymentDate()   { return paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/PaymentRequest.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {

    @SerializedName("amount")
    private double amount;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("description")
    private String description;

    public PaymentRequest(double amount, String paymentType,
                          String paymentMethod, String description) {
        this.amount        = amount;
        this.paymentType   = paymentType;
        this.paymentMethod = paymentMethod;
        this.description   = description;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/model/TinRecord.java">
package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class TinRecord {

    @SerializedName("id")
    private long id;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("taxZone")
    private String taxZone;

    @SerializedName("taxCircle")
    private String taxCircle;

    @SerializedName("issuedDate")
    private String issuedDate;

    public long   getId()         { return id; }
    public String getTinNumber()  { return tinNumber; }
    public String getTaxZone()    { return taxZone; }
    public String getTaxCircle()  { return taxCircle; }
    public String getIssuedDate() { return issuedDate; }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/repository/AuthRepository.java">
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
</file>

<file path="app/src/main/java/com/vtms/nirapod/data/repository/ItrRepository.java">
package com.vtms.nirapod.data.repository;

import com.vtms.nirapod.data.api.ItrApi;
import com.vtms.nirapod.data.model.ItrRecord;
import java.util.List;
import retrofit2.Call;

public class ItrRepository {

    private final ItrApi itrApi;

    public ItrRepository(ItrApi itrApi) {
        this.itrApi = itrApi;
    }

    public Call<List<ItrRecord>> getMyReturns() {
        return itrApi.getMyReturns();
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/auth/LoginActivity.java">
package com.vtms.nirapod.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.vtms.nirapod.databinding.ActivityLoginBinding;
import com.vtms.nirapod.ui.main.MainActivity;
import com.vtms.nirapod.util.NetworkResult;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel        viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding   = ActivityLoginBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        setContentView(binding.getRoot());

        setupClickListeners();
        observeLoginResult();
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email    = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            viewModel.login(email, password);
        });
    }

    private void observeLoginResult() {
        viewModel.getLoginResult().observe(this, result -> {

            if (result.isLoading()) {
                showLoading(true);
                hideError();
            }

            else if (result.isSuccess()) {
                showLoading(false);
                goToMain();
            }

            else if (result.isError()) {
                showLoading(false);
                showError(result.message);
            }
        });
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
    }

    private void showError(String message) {
        binding.tvError.setText(message);
        binding.tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        binding.tvError.setVisibility(View.GONE);
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/auth/LoginViewModel.java">
package com.vtms.nirapod.ui.auth;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AuthResponse;
import com.vtms.nirapod.data.repository.AuthRepository;
import com.vtms.nirapod.util.NetworkResult;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository repository;
    private final MutableLiveData<NetworkResult<AuthResponse>> loginResult
            = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application app) {
        super(app);
        TokenManager tm = new TokenManager(app);
        repository = new AuthRepository(
                RetrofitClient.authApi(tm), tm
        );
    }

    public LiveData<NetworkResult<AuthResponse>> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            loginResult.setValue(
                    NetworkResult.error("Please enter email and password"));
            return;
        }

        loginResult.setValue(NetworkResult.loading());

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                Response<AuthResponse> response =
                        repository.login(email, password).execute();

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();

                    if (!"TAXPAYER".equals(auth.getRole())) {
                        loginResult.postValue(
                                NetworkResult.error("Only taxpayers can login here"));
                        return;
                    }

                    repository.saveSession(auth);
                    loginResult.postValue(NetworkResult.success(auth));

                } else if (response.code() == 401) {
                    loginResult.postValue(
                            NetworkResult.error("Invalid email or password"));
                } else if (response.code() == 403) {
                    loginResult.postValue(
                            NetworkResult.error("Account is suspended or inactive"));
                } else {
                    loginResult.postValue(
                            NetworkResult.error("Login failed. Please try again"));
                }

            } catch (Exception e) {
                loginResult.postValue(
                        NetworkResult.error("Cannot connect to server"));
            }
        });
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/auth/SplashActivity.java">
package com.vtms.nirapod.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenManager tm = new TokenManager(this);

        Intent intent;
        if (tm.isLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
        finish();
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/home/HomeFragment.java">
package com.vtms.nirapod.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.local.TokenManager;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TokenManager tm = new TokenManager(requireContext());

        TextView tvName   = view.findViewById(R.id.tvName);
        TextView tvTin    = view.findViewById(R.id.tvTin);
        TextView tvStatus = view.findViewById(R.id.tvStatus);

        tvName.setText(tm.getUserName());
        tvTin.setText(tm.getTinNumber().isEmpty()
                ? "Not assigned" : tm.getTinNumber());
        tvStatus.setText("APPROVED");

        view.findViewById(R.id.cardItr).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            new com.vtms.nirapod.ui.tax.TaxFragment())
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.cardAit).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            new com.vtms.nirapod.ui.tax.TaxFragment())
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.cardNotice).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            new com.vtms.nirapod.ui.notice.NoticeListFragment())
                    .addToBackStack(null)
                    .commit();
        });

        view.findViewById(R.id.cardCertificate).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment,
                            new com.vtms.nirapod.ui.tax.TaxFragment())
                    .addToBackStack(null)
                    .commit();
        });
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/main/MainActivity.java">
package com.vtms.nirapod.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.ui.home.HomeFragment;
import com.vtms.nirapod.ui.notice.NoticeListFragment;
import com.vtms.nirapod.ui.payment.PayHistoryFragment;
import com.vtms.nirapod.ui.profile.ProfileFragment;
import com.vtms.nirapod.ui.tax.TaxFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Default: Home fragment
        loadFragment(new HomeFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_tax) {
                fragment = new TaxFragment();
            } else if (id == R.id.nav_payment) {
                fragment = new PayHistoryFragment();
            } else if (id == R.id.nav_notice) {
                fragment = new NoticeListFragment();
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                fragment = new HomeFragment();
            }

            loadFragment(fragment);
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.navHostFragment, fragment)
                .commit();
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/notice/NoticeAdapter.java">
package com.vtms.nirapod.ui.notice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.Notice;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeVH> {

    private List<Notice> items = new ArrayList<>();
    private OnClickListener listener;

    public interface OnClickListener {
        void onClick(Notice notice);
    }

    public void setListener(OnClickListener l) { this.listener = l; }

    public void setItems(List<Notice> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public NoticeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeVH h, int position) {
        h.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    class NoticeVH extends RecyclerView.ViewHolder {
        TextView tvSubject, tvType, tvDate, tvUnread;
        View unreadIndicator;

        NoticeVH(View v) {
            super(v);
            tvSubject        = v.findViewById(R.id.tvSubject);
            tvType           = v.findViewById(R.id.tvType);
            tvDate           = v.findViewById(R.id.tvDate);
            tvUnread         = v.findViewById(R.id.tvUnread);
            unreadIndicator  = v.findViewById(R.id.unreadIndicator);
        }

        void bind(Notice n) {
            tvSubject.setText(n.getSubject() != null
                    ? n.getSubject() : "Notice");
            tvType.setText(n.getNoticeType() != null
                    ? n.getNoticeType() : "General");

            String date = n.getCreatedAt();
            tvDate.setText(date != null && date.length() >= 10
                    ? date.substring(0, 10) : "---");

            if (!n.isRead()) {
                unreadIndicator.setBackgroundColor(
                        itemView.getContext().getResources()
                                .getColor(R.color.primary));
                tvUnread.setVisibility(View.VISIBLE);
            } else {
                unreadIndicator.setBackgroundColor(
                        itemView.getContext().getResources()
                                .getColor(R.color.divider));
                tvUnread.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onClick(n);
            });
        }
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/notice/NoticeDetailFragment.java">
package com.vtms.nirapod.ui.notice;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Notice;
import com.vtms.nirapod.data.model.NoticeReplyRequest;
import java.util.concurrent.Executors;

public class NoticeDetailFragment extends Fragment {

    private Notice notice;

    public static NoticeDetailFragment newInstance(Notice notice) {
        NoticeDetailFragment f = new NoticeDetailFragment();
        f.notice = notice;
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ScrollView scroll = new ScrollView(requireContext());
        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(requireContext().getResources()
                .getColor(R.color.background));
        scroll.addView(root);

        // Header
        TextView header = new TextView(requireContext());
        header.setText("Notice Detail");
        header.setTextSize(20f);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setTextColor(requireContext().getResources()
                .getColor(R.color.text_primary));
        root.addView(header);

        addRow(root, "Subject", notice.getSubject());
        addRow(root, "Type", notice.getNoticeType());
        addRow(root, "Date", notice.getCreatedAt() != null
                && notice.getCreatedAt().length() >= 10
                ? notice.getCreatedAt().substring(0, 10) : "---");
        addRow(root, "Body", notice.getBody() != null
                ? notice.getBody() : "No content");

        // Reply section
        View divider = new View(requireContext());
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(requireContext().getResources()
                .getColor(R.color.divider));
        LinearLayout.LayoutParams dp = (LinearLayout.LayoutParams)
                divider.getLayoutParams();
        dp.setMargins(0, 32, 0, 32);
        root.addView(divider);

        TextView replyLabel = new TextView(requireContext());
        replyLabel.setText("Reply");
        replyLabel.setTextSize(16f);
        replyLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        replyLabel.setTextColor(requireContext().getResources()
                .getColor(R.color.text_primary));
        root.addView(replyLabel);

        if (notice.getResponseText() != null) {
            TextView tvPrev = new TextView(requireContext());
            tvPrev.setText("Previous reply: " + notice.getResponseText());
            tvPrev.setTextSize(13f);
            tvPrev.setTextColor(requireContext().getResources()
                    .getColor(R.color.text_secondary));
            tvPrev.setPadding(0, 8, 0, 16);
            root.addView(tvPrev);
        }

        EditText etReply = new EditText(requireContext());
        etReply.setHint("Write your reply here...");
        etReply.setMinLines(3);
        etReply.setBackgroundColor(requireContext().getResources()
                .getColor(R.color.card_background));
        etReply.setPadding(24, 24, 24, 24);
        root.addView(etReply);

        MaterialButton btnReply = new MaterialButton(requireContext());
        btnReply.setText("Send Reply");
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(0, 24, 0, 0);
        btnReply.setLayoutParams(btnParams);
        root.addView(btnReply);

        TextView tvResult = new TextView(requireContext());
        tvResult.setVisibility(View.GONE);
        tvResult.setTextSize(13f);
        root.addView(tvResult);

        TokenManager tm = new TokenManager(requireContext());

        btnReply.setOnClickListener(v -> {
            String reply = etReply.getText().toString().trim();
            if (reply.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Please write a reply", Toast.LENGTH_SHORT).show();
                return;
            }

            btnReply.setEnabled(false);
            btnReply.setText("Sending...");

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    RetrofitClient.noticeApi(tm)
                            .markAsRead(notice.getId()).execute();

                    var resp = RetrofitClient.noticeApi(tm)
                            .replyToNotice(notice.getId(),
                                    new NoticeReplyRequest(reply)).execute();

                    requireActivity().runOnUiThread(() -> {
                        btnReply.setEnabled(true);
                        btnReply.setText("Send Reply");
                        if (resp.isSuccessful()) {
                            tvResult.setText("Reply sent successfully!");
                            tvResult.setTextColor(requireContext()
                                    .getResources().getColor(R.color.success));
                            tvResult.setVisibility(View.VISIBLE);
                            etReply.setText("");
                        } else {
                            tvResult.setText("Failed to send reply");
                            tvResult.setTextColor(requireContext()
                                    .getResources().getColor(R.color.error));
                            tvResult.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> {
                        btnReply.setEnabled(true);
                        btnReply.setText("Send Reply");
                        tvResult.setText("Cannot connect to server");
                        tvResult.setTextColor(requireContext()
                                .getResources().getColor(R.color.error));
                        tvResult.setVisibility(View.VISIBLE);
                    });
                }
            });
        });

        return scroll;
    }

    private void addRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        row.setLayoutParams(lp);

        TextView lbl = new TextView(requireContext());
        lbl.setText(label);
        lbl.setTextSize(12f);
        lbl.setTextColor(requireContext().getResources()
                .getColor(R.color.text_secondary));
        row.addView(lbl);

        TextView val = new TextView(requireContext());
        val.setText(value != null ? value : "---");
        val.setTextSize(14f);
        val.setTextColor(requireContext().getResources()
                .getColor(R.color.text_primary));
        row.addView(val);

        parent.addView(row);
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/notice/NoticeListFragment.java">
package com.vtms.nirapod.ui.notice;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Notice;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class NoticeListFragment extends Fragment {

    private NoticeAdapter adapter;
    private TokenManager  tm;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tm = new TokenManager(requireContext());

        RecyclerView rv  = view.findViewById(R.id.recyclerView);
        ProgressBar  pb  = view.findViewById(R.id.progressBar);
        TextView  tvEmpty = view.findViewById(R.id.tvEmpty);

        adapter = new NoticeAdapter();
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        adapter.setListener(notice -> {
            NoticeDetailFragment detail = NoticeDetailFragment.newInstance(notice);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.navHostFragment, detail)
                    .addToBackStack(null)
                    .commit();
        });

        pb.setVisibility(View.VISIBLE);
        loadNotices(pb, tvEmpty);
    }

    private void loadNotices(ProgressBar pb, TextView tvEmpty) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<Notice>> response =
                        RetrofitClient.noticeApi(tm).getMyNotices().execute();

                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null
                            && !response.body().isEmpty()) {
                        adapter.setItems(response.body());
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    tvEmpty.setText("Cannot connect to server");
                    tvEmpty.setVisibility(View.VISIBLE);
                });
            }
        });
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/payment/MakePaymentFragment.java">
package com.vtms.nirapod.ui.payment;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Payment;
import com.vtms.nirapod.data.model.PaymentRequest;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class MakePaymentFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_make_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText etAmount      = view.findViewById(R.id.etAmount);
        TextInputEditText etDescription = view.findViewById(R.id.etDescription);
        RadioGroup        rgType        = view.findViewById(R.id.rgPaymentType);
        RadioGroup        rgMethod      = view.findViewById(R.id.rgPaymentMethod);
        MaterialButton    btnPay        = view.findViewById(R.id.btnPay);
        ProgressBar       pb            = view.findViewById(R.id.progressBar);
        TextView          tvError       = view.findViewById(R.id.tvPayError);

        TokenManager tm = new TokenManager(requireContext());

        btnPay.setOnClickListener(v -> {
            String amountStr = etAmount.getText() != null
                    ? etAmount.getText().toString().trim() : "";

            if (amountStr.isEmpty()) {
                tvError.setText("Please enter an amount");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                tvError.setText("Please enter a valid amount");
                tvError.setVisibility(View.VISIBLE);
                return;
            }

            tvError.setVisibility(View.GONE);

            // Get selected type
            String paymentType = "Income Tax";
            int typeId = rgType.getCheckedRadioButtonId();
            if (typeId == R.id.rbVat)     paymentType = "VAT";
            else if (typeId == R.id.rbPenalty) paymentType = "Penalty";

            // Get selected method
            String method = "Bank Transfer";
            int methodId = rgMethod.getCheckedRadioButtonId();
            if (methodId == R.id.rbBkash)  method = "bKash";
            else if (methodId == R.id.rbNagad) method = "Nagad";

            String description = etDescription.getText() != null
                    ? etDescription.getText().toString().trim() : "";

            btnPay.setEnabled(false);
            pb.setVisibility(View.VISIBLE);

            final String finalType   = paymentType;
            final String finalMethod = method;
            final double finalAmount = amount;

            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    PaymentRequest req = new PaymentRequest(
                            finalAmount, finalType, finalMethod, description);

                    Response<Payment> response =
                            RetrofitClient.paymentApi(tm).makePayment(req).execute();

                    requireActivity().runOnUiThread(() -> {
                        pb.setVisibility(View.GONE);
                        btnPay.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(requireContext(),
                                    "Payment submitted successfully!",
                                    Toast.LENGTH_LONG).show();
                            etAmount.setText("");
                            etDescription.setText("");
                        } else {
                            tvError.setText("Payment failed. Please try again.");
                            tvError.setVisibility(View.VISIBLE);
                        }
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() -> {
                        pb.setVisibility(View.GONE);
                        btnPay.setEnabled(true);
                        tvError.setText("Cannot connect to server");
                        tvError.setVisibility(View.VISIBLE);
                    });
                }
            });
        });
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/payment/PayHistoryFragment.java">
package com.vtms.nirapod.ui.payment;

import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;

public class PayHistoryFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnHistory     = view.findViewById(R.id.btnHistory);
        Button btnMakePayment = view.findViewById(R.id.btnMakePayment);

        loadSub(new PaymentListFragment());

        btnHistory.setOnClickListener(v -> {
            loadSub(new PaymentListFragment());
            btnHistory.setAlpha(1f);
            btnMakePayment.setAlpha(0.6f);
        });

        btnMakePayment.setOnClickListener(v -> {
            loadSub(new MakePaymentFragment());
            btnMakePayment.setAlpha(1f);
            btnHistory.setAlpha(0.6f);
        });
    }

    private void loadSub(Fragment f) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.paymentContent, f)
                .commit();
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/payment/PaymentAdapter.java">
package com.vtms.nirapod.ui.payment;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.Payment;
import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.VH> {

    private List<Payment> items = new ArrayList<>();

    public void setItems(List<Payment> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        h.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {

        View    colorBar;
        TextView tvType, tvMethod, tvDate, tvAmount, tvStatus;

        VH(View v) {
            super(v);
            colorBar = v.findViewById(R.id.colorBar);
            tvType   = v.findViewById(R.id.tvType);
            tvMethod = v.findViewById(R.id.tvMethod);
            tvDate   = v.findViewById(R.id.tvDate);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvStatus = v.findViewById(R.id.tvStatus);
        }

        void bind(Payment p) {
            tvType.setText(p.getPaymentType() != null
                    ? p.getPaymentType() : "Payment");
            tvMethod.setText(p.getPaymentMethod() != null
                    ? p.getPaymentMethod() : "---");

            String d = p.getPaymentDate();
            tvDate.setText(d != null && d.length() >= 10
                    ? d.substring(0, 10) : "---");

            tvAmount.setText("BDT "
                    + String.format("%,.0f", p.getAmount()));

            String status = p.getStatus() != null ? p.getStatus() : "";
            tvStatus.setText(status);

            int green  = itemView.getContext().getResources()
                    .getColor(R.color.success);
            int yellow = itemView.getContext().getResources()
                    .getColor(R.color.warning);
            int red    = itemView.getContext().getResources()
                    .getColor(R.color.error);
            int blue   = itemView.getContext().getResources()
                    .getColor(R.color.primary);

            switch (status) {
                case "Completed":
                    tvStatus.setTextColor(green);
                    colorBar.setBackgroundColor(green);
                    break;
                case "Pending":
                    tvStatus.setTextColor(yellow);
                    colorBar.setBackgroundColor(yellow);
                    break;
                case "Failed":
                    tvStatus.setTextColor(red);
                    colorBar.setBackgroundColor(red);
                    break;
                default:
                    tvStatus.setTextColor(blue);
                    colorBar.setBackgroundColor(blue);
            }
        }
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/payment/PaymentListFragment.java">
package com.vtms.nirapod.ui.payment;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.Payment;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class PaymentListFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(requireContext().getResources()
                .getColor(R.color.background));

        ProgressBar pb = new ProgressBar(requireContext());
        LinearLayout.LayoutParams pbp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pbp.gravity = android.view.Gravity.CENTER_HORIZONTAL;
        pbp.setMargins(0, 80, 0, 0);
        pb.setLayoutParams(pbp);
        root.addView(pb);

        TextView tvEmpty = new TextView(requireContext());
        tvEmpty.setText("No payment records found");
        tvEmpty.setTextSize(16f);
        tvEmpty.setGravity(android.view.Gravity.CENTER);
        tvEmpty.setPadding(0, 80, 0, 0);
        tvEmpty.setTextColor(requireContext().getResources()
                .getColor(R.color.text_secondary));
        tvEmpty.setVisibility(View.GONE);
        root.addView(tvEmpty);

        RecyclerView rv = new RecyclerView(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        PaymentAdapter adapter = new PaymentAdapter();
        rv.setAdapter(adapter);
        root.addView(rv);

        TokenManager tm = new TokenManager(requireContext());

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<Payment>> response =
                        RetrofitClient.paymentApi(tm).getMyPayments().execute();

                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null
                            && !response.body().isEmpty()) {
                        adapter.setItems(response.body());
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    tvEmpty.setText("Cannot connect to server");
                    tvEmpty.setVisibility(View.VISIBLE);
                });
            }
        });

        return root;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/profile/ProfileFragment.java">
package com.vtms.nirapod.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.ui.auth.LoginActivity;

public class ProfileFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TokenManager tm = new TokenManager(requireContext());

        TextView tvAvatar  = view.findViewById(R.id.tvAvatar);
        TextView tvName    = view.findViewById(R.id.tvProfileName);
        TextView tvRole    = view.findViewById(R.id.tvProfileRole);
        TextView tvTin     = view.findViewById(R.id.tvProfileTin);
        TextView tvEmail   = view.findViewById(R.id.tvProfileEmail);
        TextView tvStatus  = view.findViewById(R.id.tvProfileStatus);

        // Avatar — নামের প্রথম অক্ষর
        String name = tm.getUserName();
        if (name != null && !name.isEmpty()) {
            tvAvatar.setText(
                    String.valueOf(name.charAt(0)).toUpperCase());
        }

        tvName.setText(name);
        tvRole.setText(tm.getUserRole());
        tvTin.setText(tm.getTinNumber().isEmpty()
                ? "Not assigned" : tm.getTinNumber());
        tvEmail.setText(tm.getUserEmail());
        tvStatus.setText("APPROVED");

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            tm.clearSession();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/AitAdapter.java">
package com.vtms.nirapod.ui.tax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.AitRecord;
import java.util.ArrayList;
import java.util.List;

public class AitAdapter extends RecyclerView.Adapter<AitAdapter.AitViewHolder> {

    private List<AitRecord> items = new ArrayList<>();

    public void setItems(List<AitRecord> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ait, parent, false);
        return new AitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AitViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class AitViewHolder extends RecyclerView.ViewHolder {

        TextView tvAitNumber, tvStatus, tvAmount, tvSource, tvBank;

        AitViewHolder(View v) {
            super(v);
            tvAitNumber = v.findViewById(R.id.tvAitNumber);
            tvStatus    = v.findViewById(R.id.tvStatus);
            tvAmount    = v.findViewById(R.id.tvAmount);
            tvSource    = v.findViewById(R.id.tvSource);
            tvBank      = v.findViewById(R.id.tvBank);
        }

        void bind(AitRecord item) {
            tvAitNumber.setText(item.getAitNumber() != null
                    ? item.getAitNumber() : "AIT Record");
            tvStatus.setText(item.getStatus());
            tvAmount.setText("BDT " + String.format("%,.0f", item.getAitAmount()));
            tvSource.setText(item.getSourceOfIncome() != null
                    ? item.getSourceOfIncome() : "---");
            tvBank.setText(item.getBankName() != null
                    ? item.getBankName() : "---");

            int color;
            switch (item.getStatus() != null ? item.getStatus() : "") {
                case "APPROVED":
                case "CREDITED":
                    color = itemView.getContext().getResources()
                            .getColor(R.color.success); break;
                case "REJECTED":
                    color = itemView.getContext().getResources()
                            .getColor(R.color.error); break;
                default:
                    color = itemView.getContext().getResources()
                            .getColor(R.color.primary);
            }
            tvStatus.getBackground().setTint(color);
        }
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/AitListFragment.java">
package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AitRecord;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AitListFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(requireContext().getResources()
                .getColor(R.color.background));

        // Header
        TextView header = new TextView(requireContext());
        header.setText("AIT Records");
        header.setTextSize(18f);
        header.setTextColor(requireContext().getResources()
                .getColor(R.color.white));
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setPadding(48, 36, 48, 36);
        header.setBackgroundColor(requireContext().getResources()
                .getColor(R.color.primary));
        root.addView(header);

        ProgressBar pb = new ProgressBar(requireContext());
        pb.setVisibility(View.GONE);
        root.addView(pb);

        TextView tvEmpty = new TextView(requireContext());
        tvEmpty.setText("No AIT records found");
        tvEmpty.setTextSize(16f);
        tvEmpty.setGravity(android.view.Gravity.CENTER);
        tvEmpty.setPadding(48, 120, 48, 0);
        tvEmpty.setVisibility(View.GONE);
        root.addView(tvEmpty);

        RecyclerView rv = new RecyclerView(requireContext());
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        AitAdapter adapter = new AitAdapter();
        rv.setAdapter(adapter);
        root.addView(rv);

        pb.setVisibility(View.VISIBLE);

        TokenManager tm = new TokenManager(requireContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<AitRecord>> response =
                        RetrofitClient.aitApi(tm).getMyAitRecords().execute();

                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null
                            && !response.body().isEmpty()) {
                        adapter.setItems(response.body());
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    tvEmpty.setText("Cannot connect to server");
                    tvEmpty.setVisibility(View.VISIBLE);
                });
            }
        });

        return root;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/BusinessFragment.java">
package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

public class BusinessFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i,
                             @Nullable ViewGroup c,
                             @Nullable Bundle s) {
        TextView tv = new TextView(requireContext());
        tv.setText("My Businesses");
        tv.setTextSize(18f);
        tv.setPadding(48, 80, 48, 0);
        return tv;
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/CertificateFragment.java">
package com.vtms.nirapod.ui.tax;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.TinRecord;
import java.io.*;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class CertificateFragment extends Fragment {

    private TokenManager tm;
    private TinRecord    myTin;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ScrollView scroll = new ScrollView(requireContext());
        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.background));
        scroll.addView(root);

        tm = new TokenManager(requireContext());

        // Header
        TextView header = new TextView(requireContext());
        header.setText("TIN Certificate");
        header.setTextSize(20f);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setTextColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.text_primary));
        root.addView(header);

        // Loading
        ProgressBar pb = new ProgressBar(requireContext());
        LinearLayout.LayoutParams pbp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pbp.setMargins(0, 48, 0, 0);
        pb.setLayoutParams(pbp);
        root.addView(pb);

        // Info card
        LinearLayout infoLayout = new LinearLayout(requireContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.setVisibility(View.GONE);
        root.addView(infoLayout);

        // Download button
        MaterialButton btnDownload = new MaterialButton(requireContext());
        btnDownload.setText("Download TIN Certificate (PDF)");
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        bp.setMargins(0, 32, 0, 0);
        btnDownload.setLayoutParams(bp);
        btnDownload.setVisibility(View.GONE);
        root.addView(btnDownload);

        TextView tvResult = new TextView(requireContext());
        tvResult.setVisibility(View.GONE);
        tvResult.setTextSize(13f);
        tvResult.setPadding(0, 16, 0, 0);
        root.addView(tvResult);

        // Load TIN data
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<TinRecord>> response =
                        RetrofitClient.tinApi(tm).getMyTins().execute();

                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);

                    if (response.isSuccessful()
                            && response.body() != null
                            && !response.body().isEmpty()) {

                        myTin = response.body().get(0);
                        showTinInfo(infoLayout, myTin);
                        infoLayout.setVisibility(View.VISIBLE);
                        btnDownload.setVisibility(View.VISIBLE);

                    } else {
                        TextView tvErr = new TextView(requireContext());
                        tvErr.setText("No TIN certificate found");
                        tvErr.setTextSize(15f);
                        tvErr.setTextColor(requireContext().getResources()
                                .getColor(com.vtms.nirapod.R.color.text_secondary));
                        tvErr.setPadding(0, 48, 0, 0);
                        root.addView(tvErr);
                    }
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    TextView tvErr = new TextView(requireContext());
                    tvErr.setText("Cannot connect to server");
                    tvErr.setTextSize(15f);
                    tvErr.setTextColor(requireContext().getResources()
                            .getColor(com.vtms.nirapod.R.color.error));
                    root.addView(tvErr);
                });
            }
        });

        btnDownload.setOnClickListener(v -> {
            if (myTin == null) return;
            btnDownload.setEnabled(false);
            btnDownload.setText("Downloading...");
            tvResult.setVisibility(View.GONE);
            downloadPdf(myTin.getId(), btnDownload, tvResult);
        });

        return scroll;
    }

    private void showTinInfo(LinearLayout layout, TinRecord tin) {
        layout.removeAllViews();
        addInfoRow(layout, "TIN Number",  tin.getTinNumber());
        addInfoRow(layout, "Tax Zone",    tin.getTaxZone());
        addInfoRow(layout, "Tax Circle",  tin.getTaxCircle());
        addInfoRow(layout, "Issued Date", tin.getIssuedDate() != null
                && tin.getIssuedDate().length() >= 10
                ? tin.getIssuedDate().substring(0, 10) : "---");
    }

    private void addInfoRow(LinearLayout parent, String label, String value) {
        LinearLayout row = new LinearLayout(requireContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 24, 0, 0);
        row.setLayoutParams(lp);

        TextView lbl = new TextView(requireContext());
        lbl.setText(label);
        lbl.setTextSize(13f);
        lbl.setTextColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.text_secondary));
        lbl.setLayoutParams(new LinearLayout.LayoutParams(280,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        row.addView(lbl);

        TextView val = new TextView(requireContext());
        val.setText(value != null ? value : "---");
        val.setTextSize(14f);
        val.setTypeface(null, android.graphics.Typeface.BOLD);
        val.setTextColor(requireContext().getResources()
                .getColor(com.vtms.nirapod.R.color.text_primary));
        row.addView(val);

        parent.addView(row);
    }

    private void downloadPdf(long tinId,
                             MaterialButton btnDownload,
                             TextView tvResult) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<okhttp3.ResponseBody> response =
                        RetrofitClient.tinApi(tm)
                                .downloadCertificate(tinId).execute();

                if (response.isSuccessful() && response.body() != null) {
                    // Save to cache directory
                    File pdfFile = new File(
                            requireContext().getCacheDir(),
                            "TIN_Certificate_" + tinId + ".pdf");

                    try (InputStream is = response.body().byteStream();
                         OutputStream os = new FileOutputStream(pdfFile)) {
                        byte[] buf = new byte[4096];
                        int len;
                        while ((len = is.read(buf)) != -1) {
                            os.write(buf, 0, len);
                        }
                    }

                    requireActivity().runOnUiThread(() -> {
                        btnDownload.setEnabled(true);
                        btnDownload.setText("Download TIN Certificate (PDF)");
                        openPdf(pdfFile, tvResult);
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        btnDownload.setEnabled(true);
                        btnDownload.setText("Download TIN Certificate (PDF)");
                        tvResult.setText("Download failed. Try again.");
                        tvResult.setTextColor(requireContext().getResources()
                                .getColor(com.vtms.nirapod.R.color.error));
                        tvResult.setVisibility(View.VISIBLE);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    btnDownload.setEnabled(true);
                    btnDownload.setText("Download TIN Certificate (PDF)");
                    tvResult.setText("Error: " + e.getMessage());
                    tvResult.setTextColor(requireContext().getResources()
                            .getColor(com.vtms.nirapod.R.color.error));
                    tvResult.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void openPdf(File file, TextView tvResult) {
        try {
            android.net.Uri uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            tvResult.setText("PDF saved. Install a PDF viewer to open it.");
            tvResult.setTextColor(requireContext().getResources()
                    .getColor(com.vtms.nirapod.R.color.warning));
            tvResult.setVisibility(View.VISIBLE);
        }
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/ItrAdapter.java">
package com.vtms.nirapod.ui.tax;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.model.ItrRecord;
import java.util.ArrayList;
import java.util.List;

public class ItrAdapter extends RecyclerView.Adapter<ItrAdapter.ItrViewHolder> {

    private List<ItrRecord> items = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ItrRecord item);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ItrRecord> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_itr, parent, false);
        return new ItrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItrViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItrViewHolder extends RecyclerView.ViewHolder {

        TextView tvYear, tvStatus, tvIncome, tvTax, tvDate;

        ItrViewHolder(View itemView) {
            super(itemView);
            tvYear   = itemView.findViewById(R.id.tvYear);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvIncome = itemView.findViewById(R.id.tvIncome);
            tvTax    = itemView.findViewById(R.id.tvTax);
            tvDate   = itemView.findViewById(R.id.tvDate);
        }

        void bind(ItrRecord item) {
            tvYear.setText("AY " + item.getAssessmentYear());
            tvStatus.setText(item.getStatus());
            tvIncome.setText("BDT " + String.format("%,.0f", item.getTotalIncome()));
            tvTax.setText("BDT " + String.format("%,.0f", item.getGrossTax()));

            String date = item.getSubmissionDate();
            tvDate.setText(date != null && date.length() >= 10
                    ? date.substring(0, 10) : "---");

            // Status badge color
            String status = item.getStatus();
            int color;
            if ("Accepted".equals(status) || "APPROVED".equals(status)) {
                color = itemView.getContext().getResources()
                        .getColor(R.color.success);
            } else if ("Rejected".equals(status)) {
                color = itemView.getContext().getResources()
                        .getColor(R.color.error);
            } else {
                color = itemView.getContext().getResources()
                        .getColor(R.color.primary);
            }
            tvStatus.getBackground().setTint(color);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item);
            });
        }
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/ItrListFragment.java">
package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vtms.nirapod.R;

public class ItrListFragment extends Fragment {

    private ItrViewModel viewModel;
    private ItrAdapter   adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itr_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ProgressBar  progressBar  = view.findViewById(R.id.progressBar);
        TextView     tvEmpty      = view.findViewById(R.id.tvEmpty);

        adapter = new ItrAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        adapter.setListener(item ->
                Toast.makeText(requireContext(),
                        "AY: " + item.getAssessmentYear()
                                + " | Status: " + item.getStatus(),
                        Toast.LENGTH_SHORT).show()
        );

        viewModel = new ViewModelProvider(this).get(ItrViewModel.class);

        viewModel.getItrResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isLoading()) {
                progressBar.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.GONE);
            } else if (result.isSuccess()) {
                progressBar.setVisibility(View.GONE);
                if (result.data == null || result.data.isEmpty()) {
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    adapter.setItems(result.data);
                }
            } else {
                progressBar.setVisibility(View.GONE);
                tvEmpty.setText(result.message);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });

        viewModel.loadMyReturns();
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/ItrViewModel.java">
package com.vtms.nirapod.ui.tax;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.ItrRecord;
import com.vtms.nirapod.data.repository.ItrRepository;
import com.vtms.nirapod.util.NetworkResult;
import java.util.List;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class ItrViewModel extends AndroidViewModel {

    private final ItrRepository repository;
    private final MutableLiveData<NetworkResult<List<ItrRecord>>> itrResult
            = new MutableLiveData<>();

    public ItrViewModel(@NonNull Application app) {
        super(app);
        TokenManager tm = new TokenManager(app);
        repository = new ItrRepository(RetrofitClient.itrApi(tm));
    }

    public LiveData<NetworkResult<List<ItrRecord>>> getItrResult() {
        return itrResult;
    }

    public void loadMyReturns() {
        itrResult.setValue(NetworkResult.loading());
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<ItrRecord>> response =
                        repository.getMyReturns().execute();
                if (response.isSuccessful() && response.body() != null) {
                    itrResult.postValue(NetworkResult.success(response.body()));
                } else {
                    itrResult.postValue(NetworkResult.error("Failed to load returns"));
                }
            } catch (Exception e) {
                itrResult.postValue(NetworkResult.error("Cannot connect to server"));
            }
        });
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/ui/tax/TaxFragment.java">
package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vtms.nirapod.R;

public class TaxFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tax, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnItr         = view.findViewById(R.id.btnItr);
        Button btnAit         = view.findViewById(R.id.btnAit);
        Button btnCertificate = view.findViewById(R.id.btnCertificate);
        Button btnBusiness    = view.findViewById(R.id.btnBusiness);

        // Default: ITR
        loadSub(new ItrListFragment());
        setActive(btnItr, btnAit, btnCertificate, btnBusiness);

        btnItr.setOnClickListener(v -> {
            loadSub(new ItrListFragment());
            setActive(btnItr, btnAit, btnCertificate, btnBusiness);
        });

        btnAit.setOnClickListener(v -> {
            loadSub(new AitListFragment());
            setActive(btnAit, btnItr, btnCertificate, btnBusiness);
        });

        btnCertificate.setOnClickListener(v -> {
            loadSub(new CertificateFragment());
            setActive(btnCertificate, btnItr, btnAit, btnBusiness);
        });

        btnBusiness.setOnClickListener(v -> {
            loadSub(new BusinessFragment());
            setActive(btnBusiness, btnItr, btnAit, btnCertificate);
        });
    }

    private void loadSub(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.taxContent, fragment)
                .commit();
    }

    private void setActive(Button active, Button... others) {
        active.setTextColor(
                requireContext().getResources().getColor(R.color.primary));
        for (Button b : others) {
            b.setTextColor(
                    requireContext().getResources().getColor(R.color.text_secondary));
        }
    }
}
</file>

<file path="app/src/main/java/com/vtms/nirapod/util/NetworkResult.java">
package com.vtms.nirapod.util;

public class NetworkResult<T> {

    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    public final Status status;
    public final T data;
    public final String message;

    private NetworkResult(Status status, T data, String message) {
        this.status  = status;
        this.data    = data;
        this.message = message;
    }

    public static <T> NetworkResult<T> loading() {
        return new NetworkResult<>(Status.LOADING, null, null);
    }

    public static <T> NetworkResult<T> success(T data) {
        return new NetworkResult<>(Status.SUCCESS, data, null);
    }

    public static <T> NetworkResult<T> error(String message) {
        return new NetworkResult<>(Status.ERROR, null, message);
    }

    public boolean isLoading() { return status == Status.LOADING; }
    public boolean isSuccess() { return status == Status.SUCCESS; }
    public boolean isError()   { return status == Status.ERROR;   }
}
</file>

<file path="app/src/main/res/drawable/ic_launcher_background.xml">
<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#3DDC84"
        android:pathData="M0,0h108v108h-108z" />
    <path
        android:fillColor="#00000000"
        android:pathData="M9,0L9,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,0L19,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,0L29,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,0L39,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,0L49,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,0L59,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,0L69,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,0L79,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M89,0L89,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M99,0L99,108"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,9L108,9"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,19L108,19"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,29L108,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,39L108,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,49L108,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,59L108,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,69L108,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,79L108,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,89L108,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M0,99L108,99"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,29L89,29"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,39L89,39"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,49L89,49"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,59L89,59"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,69L89,69"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M19,79L89,79"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M29,19L29,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M39,19L39,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M49,19L49,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M59,19L59,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M69,19L69,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
    <path
        android:fillColor="#00000000"
        android:pathData="M79,19L79,89"
        android:strokeWidth="0.8"
        android:strokeColor="#33FFFFFF" />
</vector>
</file>

<file path="app/src/main/res/drawable/ic_launcher_foreground.xml">
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path android:pathData="M31,63.928c0,0 6.4,-11 12.1,-13.1c7.2,-2.6 26,-1.4 26,-1.4l38.1,38.1L107,108.928l-32,-1L31,63.928z">
        <aapt:attr name="android:fillColor">
            <gradient
                android:endX="85.84757"
                android:endY="92.4963"
                android:startX="42.9492"
                android:startY="49.59793"
                android:type="linear">
                <item
                    android:color="#44000000"
                    android:offset="0.0" />
                <item
                    android:color="#00000000"
                    android:offset="1.0" />
            </gradient>
        </aapt:attr>
    </path>
    <path
        android:fillColor="#FFFFFF"
        android:fillType="nonZero"
        android:pathData="M65.3,45.828l3.8,-6.6c0.2,-0.4 0.1,-0.9 -0.3,-1.1c-0.4,-0.2 -0.9,-0.1 -1.1,0.3l-3.9,6.7c-6.3,-2.8 -13.4,-2.8 -19.7,0l-3.9,-6.7c-0.2,-0.4 -0.7,-0.5 -1.1,-0.3C38.8,38.328 38.7,38.828 38.9,39.228l3.8,6.6C36.2,49.428 31.7,56.028 31,63.928h46C76.3,56.028 71.8,49.428 65.3,45.828zM43.4,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2c-0.3,-0.7 -0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C45.3,56.528 44.5,57.328 43.4,57.328L43.4,57.328zM64.6,57.328c-0.8,0 -1.5,-0.5 -1.8,-1.2s-0.1,-1.5 0.4,-2.1c0.5,-0.5 1.4,-0.7 2.1,-0.4c0.7,0.3 1.2,1 1.2,1.8C66.5,56.528 65.6,57.328 64.6,57.328L64.6,57.328z"
        android:strokeWidth="1"
        android:strokeColor="#00000000" />
</vector>
</file>

<file path="app/src/main/res/drawable/status_badge_bg.xml">
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle">
    <solid android:color="@color/primary"/>
    <corners android:radius="20dp"/>
</shape>
</file>

<file path="app/src/main/res/layout/activity_login.xml">
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="24dp"
        android:gravity="center_horizontal">

        <!-- Logo / Header -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:gravity="center"
            android:paddingTop="60dp"
            android:paddingBottom="40dp">

            <TextView
                android:layout_width="72dp"
                android:layout_height="72dp"
                android:text="🏛"
                android:textSize="48sp"
                android:gravity="center"/>

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="NIRAPOD"
                android:textSize="28sp"
                android:textStyle="bold"
                android:textColor="@color/primary"
                android:layout_marginTop="12dp"/>

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="National VAT &amp; Tax Management"
                android:textSize="13sp"
                android:textColor="@color/text_secondary"
                android:layout_marginTop="4dp"/>

        </LinearLayout>

        <!-- Login Card -->
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:cardCornerRadius="16dp"
            app:cardElevation="4dp"
            app:cardBackgroundColor="@color/card_background"
            xmlns:app="http://schemas.android.com/apk/res-auto">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="24dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Taxpayer Login"
                    android:textSize="20sp"
                    android:textStyle="bold"
                    android:textColor="@color/text_primary"
                    android:layout_marginBottom="24dp"/>

                <!-- Email -->
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/tilEmail"
                    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Email Address"
                    android:layout_marginBottom="16dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etEmail"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="textEmailAddress"
                        android:imeOptions="actionNext"/>

                </com.google.android.material.textfield.TextInputLayout>

                <!-- Password -->
                <com.google.android.material.textfield.TextInputLayout
                    android:id="@+id/tilPassword"
                    style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Password"
                    app:passwordToggleEnabled="true"
                    android:layout_marginBottom="24dp">

                    <com.google.android.material.textfield.TextInputEditText
                        android:id="@+id/etPassword"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="textPassword"
                        android:imeOptions="actionDone"/>

                </com.google.android.material.textfield.TextInputLayout>

                <!-- Error message -->
                <TextView
                    android:id="@+id/tvError"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text=""
                    android:textColor="@color/error"
                    android:textSize="13sp"
                    android:visibility="gone"
                    android:layout_marginBottom="16dp"/>

                <!-- Login Button -->
                <com.google.android.material.button.MaterialButton
                    android:id="@+id/btnLogin"
                    android:layout_width="match_parent"
                    android:layout_height="56dp"
                    android:text="Login"
                    android:textSize="16sp"
                    android:textStyle="bold"
                    app:backgroundTint="@color/primary"
                    app:cornerRadius="12dp"/>

                <!-- Loading -->
                <ProgressBar
                    android:id="@+id/progressBar"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center"
                    android:layout_marginTop="16dp"
                    android:visibility="gone"/>

            </LinearLayout>
        </androidx.cardview.widget.CardView>

        <!-- Footer -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="National Board of Revenue, Bangladesh"
            android:textSize="11sp"
            android:textColor="@color/text_secondary"
            android:layout_marginTop="32dp"
            android:layout_marginBottom="24dp"/>

    </LinearLayout>
</ScrollView>
</file>

<file path="app/src/main/res/layout/activity_main.xml">
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/background">

    <!-- Fragment container -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/navHostFragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <!-- Bottom Navigation -->
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottomNav"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/card_background"
        app:menu="@menu/bottom_nav_menu"
        app:itemIconTint="@color/primary"
        app:itemTextColor="@color/text_secondary"
        app:labelVisibilityMode="labeled"/>

</LinearLayout>
</file>

<file path="app/src/main/res/layout/fragment_home.xml">
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <!-- Header Card -->
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginBottom="16dp"
            app:cardCornerRadius="16dp"
            app:cardElevation="4dp"
            app:cardBackgroundColor="@color/primary">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="20dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Welcome back,"
                    android:textColor="#B3FFFFFF"
                    android:textSize="14sp"/>

                <TextView
                    android:id="@+id/tvName"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Loading..."
                    android:textColor="@color/white"
                    android:textSize="22sp"
                    android:textStyle="bold"
                    android:layout_marginTop="4dp"/>

                <View
                    android:layout_width="match_parent"
                    android:layout_height="1dp"
                    android:background="#33FFFFFF"
                    android:layout_marginTop="12dp"
                    android:layout_marginBottom="12dp"/>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <LinearLayout
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:orientation="vertical">

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="TIN Number"
                            android:textColor="#B3FFFFFF"
                            android:textSize="12sp"/>

                        <TextView
                            android:id="@+id/tvTin"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="---"
                            android:textColor="@color/white"
                            android:textSize="14sp"
                            android:textStyle="bold"/>
                    </LinearLayout>

                    <LinearLayout
                        android:layout_width="0dp"
                        android:layout_height="wrap_content"
                        android:layout_weight="1"
                        android:orientation="vertical">

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Status"
                            android:textColor="#B3FFFFFF"
                            android:textSize="12sp"/>

                        <TextView
                            android:id="@+id/tvStatus"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="---"
                            android:textColor="#00E676"
                            android:textSize="14sp"
                            android:textStyle="bold"/>
                    </LinearLayout>

                </LinearLayout>
            </LinearLayout>
        </androidx.cardview.widget.CardView>

        <!-- Quick Actions -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Quick Actions"
            android:textSize="16sp"
            android:textStyle="bold"
            android:textColor="@color/text_primary"
            android:layout_marginBottom="12dp"/>

        <GridLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:columnCount="2"
            android:rowCount="2"
            android:layout_marginBottom="16dp">

            <!-- ITR -->
            <androidx.cardview.widget.CardView
                android:id="@+id/cardItr"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_columnWeight="1"
                android:layout_margin="6dp"
                app:cardCornerRadius="12dp"
                app:cardElevation="2dp"
                app:cardBackgroundColor="@color/card_background">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:gravity="center"
                    android:padding="12dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="📋"
                        android:textSize="28sp"/>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="ITR Returns"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:textColor="@color/text_primary"
                        android:layout_marginTop="6dp"/>
                </LinearLayout>
            </androidx.cardview.widget.CardView>

            <!-- AIT -->
            <androidx.cardview.widget.CardView
                android:id="@+id/cardAit"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_columnWeight="1"
                android:layout_margin="6dp"
                app:cardCornerRadius="12dp"
                app:cardElevation="2dp"
                app:cardBackgroundColor="@color/card_background">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:gravity="center"
                    android:padding="12dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="💰"
                        android:textSize="28sp"/>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="AIT Records"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:textColor="@color/text_primary"
                        android:layout_marginTop="6dp"/>
                </LinearLayout>
            </androidx.cardview.widget.CardView>

            <!-- Notices -->
            <androidx.cardview.widget.CardView
                android:id="@+id/cardNotice"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_columnWeight="1"
                android:layout_margin="6dp"
                app:cardCornerRadius="12dp"
                app:cardElevation="2dp"
                app:cardBackgroundColor="@color/card_background">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:gravity="center"
                    android:padding="12dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="🔔"
                        android:textSize="28sp"/>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Notices"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:textColor="@color/text_primary"
                        android:layout_marginTop="6dp"/>
                </LinearLayout>
            </androidx.cardview.widget.CardView>

            <!-- Certificate -->
            <androidx.cardview.widget.CardView
                android:id="@+id/cardCertificate"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_columnWeight="1"
                android:layout_margin="6dp"
                app:cardCornerRadius="12dp"
                app:cardElevation="2dp"
                app:cardBackgroundColor="@color/card_background">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="vertical"
                    android:gravity="center"
                    android:padding="12dp">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="📜"
                        android:textSize="28sp"/>

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Certificate"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:textColor="@color/text_primary"
                        android:layout_marginTop="6dp"/>
                </LinearLayout>
            </androidx.cardview.widget.CardView>

        </GridLayout>

    </LinearLayout>
</ScrollView>
</file>

<file path="app/src/main/res/layout/fragment_itr_list.xml">
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:orientation="horizontal"
        android:gravity="center_vertical"
        android:paddingHorizontal="16dp"
        android:background="@color/primary">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Income Tax Returns"
            android:textColor="@color/white"
            android:textSize="18sp"
            android:textStyle="bold"/>
    </LinearLayout>

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_marginTop="40dp"
        android:visibility="gone"/>

    <TextView
        android:id="@+id/tvEmpty"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="No ITR records found"
        android:textSize="16sp"
        android:textColor="@color/text_secondary"
        android:gravity="center"
        android:layout_marginTop="60dp"
        android:visibility="gone"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingTop="8dp"
        android:paddingBottom="8dp"
        android:clipToPadding="false"/>

</LinearLayout>
</file>

<file path="app/src/main/res/layout/fragment_make_payment.xml">
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="56dp"
            android:gravity="center_vertical"
            android:paddingHorizontal="16dp"
            android:background="@color/primary">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Make Payment"
                android:textColor="@color/white"
                android:textSize="18sp"
                android:textStyle="bold"/>
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="16dp">

            <androidx.cardview.widget.CardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:cardCornerRadius="12dp"
                app:cardElevation="2dp"
                app:cardBackgroundColor="@color/card_background">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="16dp">

                    <!-- Amount -->
                    <com.google.android.material.textfield.TextInputLayout
                        android:id="@+id/tilAmount"
                        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="Amount (BDT)"
                        android:layout_marginBottom="16dp">

                        <com.google.android.material.textfield.TextInputEditText
                            android:id="@+id/etAmount"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:inputType="numberDecimal"/>

                    </com.google.android.material.textfield.TextInputLayout>

                    <!-- Payment Type -->
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Payment Type"
                        android:textSize="13sp"
                        android:textColor="@color/text_secondary"
                        android:layout_marginBottom="8dp"/>

                    <RadioGroup
                        android:id="@+id/rgPaymentType"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:layout_marginBottom="16dp">

                        <RadioButton
                            android:id="@+id/rbIncomeTax"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Income Tax"
                            android:checked="true"/>

                        <RadioButton
                            android:id="@+id/rbVat"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="VAT"/>

                        <RadioButton
                            android:id="@+id/rbPenalty"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Penalty"/>

                    </RadioGroup>

                    <!-- Payment Method -->
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Payment Method"
                        android:textSize="13sp"
                        android:textColor="@color/text_secondary"
                        android:layout_marginBottom="8dp"/>

                    <RadioGroup
                        android:id="@+id/rgPaymentMethod"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:layout_marginBottom="16dp">

                        <RadioButton
                            android:id="@+id/rbBankTransfer"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Bank Transfer"
                            android:checked="true"/>

                        <RadioButton
                            android:id="@+id/rbBkash"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="bKash"/>

                        <RadioButton
                            android:id="@+id/rbNagad"
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:text="Nagad"/>

                    </RadioGroup>

                    <!-- Description -->
                    <com.google.android.material.textfield.TextInputLayout
                        android:id="@+id/tilDescription"
                        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="Description (optional)"
                        android:layout_marginBottom="24dp">

                        <com.google.android.material.textfield.TextInputEditText
                            android:id="@+id/etDescription"
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:minLines="2"/>

                    </com.google.android.material.textfield.TextInputLayout>

                    <TextView
                        android:id="@+id/tvPayError"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:textColor="@color/error"
                        android:textSize="13sp"
                        android:visibility="gone"
                        android:layout_marginBottom="12dp"/>

                    <com.google.android.material.button.MaterialButton
                        android:id="@+id/btnPay"
                        android:layout_width="match_parent"
                        android:layout_height="52dp"
                        android:text="Submit Payment"
                        android:textSize="16sp"
                        app:backgroundTint="@color/primary"
                        app:cornerRadius="12dp"/>

                    <ProgressBar
                        android:id="@+id/progressBar"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="center_horizontal"
                        android:layout_marginTop="12dp"
                        android:visibility="gone"/>

                </LinearLayout>
            </androidx.cardview.widget.CardView>

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="⚠ This is a simulated payment for demonstration purposes."
                android:textSize="12sp"
                android:textColor="@color/warning"
                android:gravity="center"
                android:layout_marginTop="16dp"/>

        </LinearLayout>
    </LinearLayout>
</ScrollView>
</file>

<file path="app/src/main/res/layout/fragment_notice_list.xml">
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:gravity="center_vertical"
        android:paddingHorizontal="16dp"
        android:background="@color/primary">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Notices"
            android:textColor="@color/white"
            android:textSize="18sp"
            android:textStyle="bold"/>
    </LinearLayout>

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="40dp"
        android:visibility="gone"/>

    <TextView
        android:id="@+id/tvEmpty"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="No notices found"
        android:textSize="16sp"
        android:textColor="@color/text_secondary"
        android:gravity="center"
        android:layout_marginTop="60dp"
        android:visibility="gone"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingTop="8dp"
        android:paddingBottom="8dp"
        android:clipToPadding="false"/>

</LinearLayout>
</file>

<file path="app/src/main/res/layout/fragment_payment.xml">
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/background">

    <!-- Tab switcher -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:orientation="horizontal"
        android:background="@color/primary">

        <Button
            android:id="@+id/btnHistory"
            style="@style/Widget.MaterialComponents.Button.TextButton"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="History"
            android:textColor="@color/white"
            android:textStyle="bold"/>

        <Button
            android:id="@+id/btnMakePayment"
            style="@style/Widget.MaterialComponents.Button.TextButton"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:text="Make Payment"
            android:textColor="#B3FFFFFF"/>
    </LinearLayout>

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="40dp"
        android:visibility="gone"/>

    <TextView
        android:id="@+id/tvEmpty"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="No payment records found"
        android:textSize="16sp"
        android:textColor="@color/text_secondary"
        android:gravity="center"
        android:layout_marginTop="60dp"
        android:visibility="gone"/>

    <FrameLayout
        android:id="@+id/paymentContent"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
</file>

<file path="app/src/main/res/layout/fragment_profile.xml">
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical">

        <!-- Header -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="180dp"
            android:orientation="vertical"
            android:gravity="center"
            android:background="@color/primary">

            <!-- Avatar circle -->
            <TextView
                android:id="@+id/tvAvatar"
                android:layout_width="72dp"
                android:layout_height="72dp"
                android:gravity="center"
                android:textSize="28sp"
                android:textStyle="bold"
                android:textColor="@color/primary"
                android:background="@color/white"
                android:layout_marginBottom="12dp"/>


            <TextView
                android:id="@+id/tvProfileName"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="---"
                android:textColor="@color/white"
                android:textSize="20sp"
                android:textStyle="bold"/>

            <TextView
                android:id="@+id/tvProfileRole"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Taxpayer"
                android:textColor="#B3FFFFFF"
                android:textSize="13sp"
                android:layout_marginTop="4dp"/>
        </LinearLayout>

        <!-- Info Cards -->
        <androidx.cardview.widget.CardView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="16dp"
            app:cardCornerRadius="12dp"
            app:cardElevation="2dp"
            app:cardBackgroundColor="@color/card_background">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical"
                android:padding="16dp">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Account Information"
                    android:textSize="14sp"
                    android:textStyle="bold"
                    android:textColor="@color/primary"
                    android:layout_marginBottom="12dp"/>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:layout_marginBottom="12dp">

                    <TextView
                        android:layout_width="120dp"
                        android:layout_height="wrap_content"
                        android:text="TIN Number"
                        android:textSize="13sp"
                        android:textColor="@color/text_secondary"/>

                    <TextView
                        android:id="@+id/tvProfileTin"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="---"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:textColor="@color/text_primary"/>
                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:layout_marginBottom="12dp">

                    <TextView
                        android:layout_width="120dp"
                        android:layout_height="wrap_content"
                        android:text="Email"
                        android:textSize="13sp"
                        android:textColor="@color/text_secondary"/>

                    <TextView
                        android:id="@+id/tvProfileEmail"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="---"
                        android:textSize="13sp"
                        android:textColor="@color/text_primary"/>
                </LinearLayout>

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">

                    <TextView
                        android:layout_width="120dp"
                        android:layout_height="wrap_content"
                        android:text="Status"
                        android:textSize="13sp"
                        android:textColor="@color/text_secondary"/>

                    <TextView
                        android:id="@+id/tvProfileStatus"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="APPROVED"
                        android:textSize="13sp"
                        android:textStyle="bold"
                        android:textColor="@color/success"/>
                </LinearLayout>
            </LinearLayout>
        </androidx.cardview.widget.CardView>

        <!-- Logout Button -->
        <com.google.android.material.button.MaterialButton
            android:id="@+id/btnLogout"
            android:layout_width="match_parent"
            android:layout_height="52dp"
            android:layout_marginHorizontal="16dp"
            android:layout_marginTop="8dp"
            android:text="Logout"
            android:textSize="16sp"
            app:backgroundTint="@color/error"
            app:cornerRadius="12dp"/>

    </LinearLayout>
</ScrollView>
</file>

<file path="app/src/main/res/layout/fragment_tax.xml">
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="@color/background">

    <!-- Sub tabs -->
    <HorizontalScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/card_background"
        android:elevation="4dp">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="56dp"
            android:orientation="horizontal"
            android:paddingStart="8dp"
            android:paddingEnd="8dp">

            <Button
                android:id="@+id/btnItr"
                style="@style/Widget.MaterialComponents.Button.TextButton"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:text="ITR Returns"
                android:textColor="@color/primary"/>

            <Button
                android:id="@+id/btnAit"
                style="@style/Widget.MaterialComponents.Button.TextButton"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:text="AIT Records"
                android:textColor="@color/text_secondary"/>

            <Button
                android:id="@+id/btnCertificate"
                style="@style/Widget.MaterialComponents.Button.TextButton"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:text="Certificate"
                android:textColor="@color/text_secondary"/>

            <Button
                android:id="@+id/btnBusiness"
                style="@style/Widget.MaterialComponents.Button.TextButton"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:text="Businesses"
                android:textColor="@color/text_secondary"/>

        </LinearLayout>
    </HorizontalScrollView>

    <!-- Content area -->
    <FrameLayout
        android:id="@+id/taxContent"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</LinearLayout>
</file>

<file path="app/src/main/res/layout/item_ait.xml">
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginVertical="6dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="2dp"
    app:cardBackgroundColor="@color/card_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/tvAitNumber"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="AIT-2425-001"
                android:textSize="15sp"
                android:textStyle="bold"
                android:textColor="@color/text_primary"/>

            <TextView
                android:id="@+id/tvStatus"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="SUBMITTED"
                android:textSize="11sp"
                android:textStyle="bold"
                android:paddingHorizontal="10dp"
                android:paddingVertical="4dp"
                android:background="@drawable/status_badge_bg"
                android:textColor="@color/white"/>
        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/divider"
            android:layout_marginVertical="10dp"/>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="AIT Amount"
                    android:textSize="11sp"
                    android:textColor="@color/text_secondary"/>

                <TextView
                    android:id="@+id/tvAmount"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="BDT 0"
                    android:textSize="14sp"
                    android:textStyle="bold"
                    android:textColor="@color/primary"/>
            </LinearLayout>

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Source"
                    android:textSize="11sp"
                    android:textColor="@color/text_secondary"/>

                <TextView
                    android:id="@+id/tvSource"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="---"
                    android:textSize="12sp"
                    android:textColor="@color/text_primary"/>
            </LinearLayout>

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Bank"
                    android:textSize="11sp"
                    android:textColor="@color/text_secondary"/>

                <TextView
                    android:id="@+id/tvBank"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="---"
                    android:textSize="12sp"
                    android:textColor="@color/text_primary"/>
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>
</androidx.cardview.widget.CardView>
</file>

<file path="app/src/main/res/layout/item_itr.xml">
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginVertical="6dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="2dp"
    app:cardBackgroundColor="@color/card_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/tvYear"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="AY 2024-25"
                android:textSize="16sp"
                android:textStyle="bold"
                android:textColor="@color/text_primary"/>

            <TextView
                android:id="@+id/tvStatus"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Submitted"
                android:textSize="12sp"
                android:textStyle="bold"
                android:paddingHorizontal="10dp"
                android:paddingVertical="4dp"
                android:background="@drawable/status_badge_bg"
                android:textColor="@color/white"/>

        </LinearLayout>

        <View
            android:layout_width="match_parent"
            android:layout_height="1dp"
            android:background="@color/divider"
            android:layout_marginVertical="10dp"/>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Total Income"
                    android:textSize="11sp"
                    android:textColor="@color/text_secondary"/>

                <TextView
                    android:id="@+id/tvIncome"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="৳ 0"
                    android:textSize="14sp"
                    android:textStyle="bold"
                    android:textColor="@color/text_primary"/>
            </LinearLayout>

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Tax Amount"
                    android:textSize="11sp"
                    android:textColor="@color/text_secondary"/>

                <TextView
                    android:id="@+id/tvTax"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="৳ 0"
                    android:textSize="14sp"
                    android:textStyle="bold"
                    android:textColor="@color/primary"/>
            </LinearLayout>

            <LinearLayout
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Filed Date"
                    android:textSize="11sp"
                    android:textColor="@color/text_secondary"/>

                <TextView
                    android:id="@+id/tvDate"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="---"
                    android:textSize="12sp"
                    android:textColor="@color/text_primary"/>
            </LinearLayout>
        </LinearLayout>
    </LinearLayout>
</androidx.cardview.widget.CardView>
</file>

<file path="app/src/main/res/layout/item_notice.xml">
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginVertical="6dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="2dp"
    app:cardBackgroundColor="@color/card_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp">

        <View
            android:id="@+id/unreadIndicator"
            android:layout_width="4dp"
            android:layout_height="match_parent"
            android:background="@color/primary"
            android:layout_marginEnd="12dp"/>

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/tvSubject"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="Notice Subject"
                android:textSize="14sp"
                android:textStyle="bold"
                android:textColor="@color/text_primary"/>

            <TextView
                android:id="@+id/tvType"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="General"
                android:textSize="12sp"
                android:textColor="@color/text_secondary"
                android:layout_marginTop="4dp"/>

            <TextView
                android:id="@+id/tvDate"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="---"
                android:textSize="11sp"
                android:textColor="@color/text_secondary"
                android:layout_marginTop="2dp"/>
        </LinearLayout>

        <TextView
            android:id="@+id/tvUnread"
            android:layout_width="20dp"
            android:layout_height="20dp"
            android:text="N"
            android:textSize="10sp"
            android:textColor="@color/white"
            android:gravity="center"
            android:background="@drawable/status_badge_bg"/>

    </LinearLayout>
</androidx.cardview.widget.CardView>
</file>

<file path="app/src/main/res/layout/item_payment.xml">
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginHorizontal="16dp"
    android:layout_marginVertical="6dp"
    app:cardCornerRadius="12dp"
    app:cardElevation="2dp"
    app:cardBackgroundColor="@color/card_background">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        android:padding="16dp"
        android:gravity="center_vertical">

        <!-- Left: Color bar -->
        <View
            android:id="@+id/colorBar"
            android:layout_width="4dp"
            android:layout_height="48dp"
            android:background="@color/primary"
            android:layout_marginEnd="14dp"/>

        <!-- Middle: Info -->
        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/tvType"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Income Tax"
                android:textSize="15sp"
                android:textStyle="bold"
                android:textColor="@color/text_primary"/>

            <TextView
                android:id="@+id/tvMethod"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Bank Transfer"
                android:textSize="12sp"
                android:textColor="@color/text_secondary"
                android:layout_marginTop="2dp"/>

            <TextView
                android:id="@+id/tvDate"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="---"
                android:textSize="11sp"
                android:textColor="@color/text_secondary"
                android:layout_marginTop="2dp"/>
        </LinearLayout>

        <!-- Right: Amount + Status -->
        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:gravity="end">

            <TextView
                android:id="@+id/tvAmount"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="BDT 0"
                android:textSize="15sp"
                android:textStyle="bold"
                android:textColor="@color/primary"/>

            <TextView
                android:id="@+id/tvStatus"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Completed"
                android:textSize="11sp"
                android:textStyle="bold"
                android:textColor="@color/success"
                android:layout_marginTop="4dp"/>
        </LinearLayout>

    </LinearLayout>
</androidx.cardview.widget.CardView>
</file>

<file path="app/src/main/res/menu/bottom_nav_menu.xml">
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/nav_home"
        android:icon="@android:drawable/ic_menu_today"
        android:title="Home"/>

    <item
        android:id="@+id/nav_tax"
        android:icon="@android:drawable/ic_menu_agenda"
        android:title="Tax"/>

    <item
        android:id="@+id/nav_payment"
        android:icon="@android:drawable/ic_menu_send"
        android:title="Payment"/>

    <item
        android:id="@+id/nav_notice"
        android:icon="@android:drawable/ic_menu_info_details"
        android:title="Notices"/>

    <item
        android:id="@+id/nav_profile"
        android:icon="@android:drawable/ic_menu_myplaces"
        android:title="Profile"/>

</menu>
</file>

<file path="app/src/main/res/mipmap-anydpi/ic_launcher_round.xml">
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
</file>

<file path="app/src/main/res/mipmap-anydpi/ic_launcher.xml">
<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
</file>

<file path="app/src/main/res/values-night/themes.xml">
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.Nirapod" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
    </style>
</resources>
</file>

<file path="app/src/main/res/values/colors.xml">
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">#1565C0</color>
    <color name="primary_dark">#0D47A1</color>
    <color name="secondary">#FF6F00</color>
    <color name="white">#FFFFFF</color>
    <color name="background">#F5F7FA</color>
    <color name="text_primary">#1A1A2E</color>
    <color name="text_secondary">#666666</color>
    <color name="success">#2E7D32</color>
    <color name="warning">#F57F17</color>
    <color name="error">#C62828</color>
    <color name="card_background">#FFFFFF</color>
    <color name="divider">#E0E0E0</color>
</resources>
</file>

<file path="app/src/main/res/values/strings.xml">
<resources>
    <string name="app_name">Nirapod</string>
</resources>
</file>

<file path="app/src/main/res/values/themes.xml">
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.Nirapod"
        parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryVariant">@color/primary_dark</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/secondary</item>
        <item name="colorOnSecondary">@color/white</item>
        <item name="android:statusBarColor">@color/primary_dark</item>
        <item name="android:windowBackground">@color/background</item>
    </style>
</resources>
</file>

<file path="app/src/main/res/xml/backup_rules.xml">
<?xml version="1.0" encoding="utf-8"?><!--
   Sample backup rules file; uncomment and customize as necessary.
   See https://developer.android.com/guide/topics/data/autobackup
   for details.
   Note: This file is ignored for devices older than API 31
   See https://developer.android.com/about/versions/12/backup-restore
-->
<full-backup-content>
    <!--
   <include domain="sharedpref" path="."/>
   <exclude domain="sharedpref" path="device.xml"/>
-->
</full-backup-content>
</file>

<file path="app/src/main/res/xml/data_extraction_rules.xml">
<?xml version="1.0" encoding="utf-8"?><!--
   Sample data extraction rules file; uncomment and customize as necessary.
   See https://developer.android.com/about/versions/12/backup-restore#xml-changes
   for details.
-->
<data-extraction-rules>
    <cloud-backup>
        <!-- TODO: Use <include> and <exclude> to control what is backed up.
        <include .../>
        <exclude .../>
        -->
    </cloud-backup>
    <!--
    <device-transfer>
        <include .../>
        <exclude .../>
    </device-transfer>
    -->
</data-extraction-rules>
</file>

<file path="app/src/main/res/xml/file_paths.xml">
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <cache-path
        name="cached_files"
        path="."/>
</paths>
</file>

<file path="app/src/test/java/com/vtms/nirapod/ExampleUnitTest.java">
package com.vtms.nirapod;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
</file>

</files>
