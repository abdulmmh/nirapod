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