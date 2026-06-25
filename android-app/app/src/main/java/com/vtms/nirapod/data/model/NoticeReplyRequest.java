package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class NoticeReplyRequest {

    @SerializedName("responseNote")
    private String responseNote;

    public NoticeReplyRequest(String responseNote) {
        this.responseNote = responseNote;
    }
}