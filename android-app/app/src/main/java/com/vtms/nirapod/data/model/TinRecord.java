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