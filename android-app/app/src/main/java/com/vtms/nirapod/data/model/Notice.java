package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class Notice {

    @SerializedName("id")
    private long id;

    @SerializedName("noticeNo")
    private String noticeNo;

    @SerializedName("subject")
    private String subject;

    @SerializedName("body")
    private String body;

    @SerializedName("noticeType")
    private String noticeType;

    @SerializedName("priority")
    private String priority;

    @SerializedName("status")
    private String status;

    @SerializedName("taxpayerName")
    private String taxpayerName;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("issuedBy")
    private String issuedBy;

    @SerializedName("targetType")
    private String targetType;

    @SerializedName("isRead")
    private boolean isRead;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("responseText")
    private String responseText;

    public long   getId()           { return id; }
    public String getNoticeNo()     { return noticeNo; }
    public String getSubject()      { return subject; }
    public String getBody()         { return body; }
    public String getNoticeType()   { return noticeType; }
    public String getPriority()     { return priority; }
    public String getStatus()       { return status; }
    public String getTaxpayerName() { return taxpayerName; }
    public String getTinNumber()    { return tinNumber; }
    public String getIssuedBy()     { return issuedBy; }
    public String getTargetType()   { return targetType; }
    public boolean isRead()         { return isRead; }
    public String getCreatedAt()    { return createdAt; }
    public String getResponseText() { return responseText; }
}
