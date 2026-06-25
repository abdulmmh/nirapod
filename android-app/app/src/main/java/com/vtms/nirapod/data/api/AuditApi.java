package com.vtms.nirapod.data.api;

import com.vtms.nirapod.data.model.Assessment;
import com.vtms.nirapod.data.model.AuditCase;
import com.vtms.nirapod.data.model.AuditDocumentRequest;
import com.vtms.nirapod.data.model.AuditQuery;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuditApi {

    @GET("api/my-portal/audits/my")
    Call<List<AuditCase>> getMyAudits();

    @GET("api/my-portal/audits/{id}")
    Call<AuditCase> getMyAuditById(@Path("id") long id);

    @GET("api/audits/{id}/queries")
    Call<List<AuditQuery>> getQueries(@Path("id") long id);

    @GET("api/audits/{id}/document-requests")
    Call<List<AuditDocumentRequest>> getDocumentRequests(@Path("id") long id);

    @GET("api/my-portal/audits/{id}/assessment")
    Call<Assessment> getMyAssessment(@Path("id") long id);

    @POST("api/my-portal/audits/{id}/respond")
    Call<Void> respond(@Path("id") long id, @Body Map<String, Object> body);

    @Multipart
    @POST("api/my-portal/audits/{id}/upload-documents")
    Call<Void> uploadDocuments(
            @Path("id") long id,
            @Part List<MultipartBody.Part> files,
            @Part("documentRequestId") RequestBody documentRequestId,
            @Part("description") RequestBody description
    );
}
