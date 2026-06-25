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