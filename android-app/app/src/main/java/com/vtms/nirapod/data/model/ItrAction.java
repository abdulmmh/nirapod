package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class ItrAction {
    @SerializedName("id")
    private long id;

    @SerializedName("action")
    private String action;

    @SerializedName("status")
    private String status;

    @SerializedName("remarks")
    private String remarks;

    @SerializedName("fromStatus")
    private String fromStatus;

    @SerializedName("toStatus")
    private String toStatus;

    @SerializedName("performedBy")
    private String performedBy;

    @SerializedName("role")
    private String role;

    @SerializedName("performedAt")
    private String performedAt;

    public long getId() { return id; }
    public String getAction() { return action; }
    public String getStatus() { return status; }
    public String getRemarks() { return remarks; }
    public String getFromStatus() { return fromStatus; }
    public String getToStatus() { return toStatus; }
    public String getPerformedBy() { return performedBy; }
    public String getRole() { return role; }
    public String getPerformedAt() { return performedAt; }
}
