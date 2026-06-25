package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class BusinessVatStatus {

    @SerializedName("id")
    private long id;

    @SerializedName("businessName")
    private String businessName;

    @SerializedName("businessTypeName")
    private String businessTypeName;

    @SerializedName("businessCategoryName")
    private String businessCategoryName;

    @SerializedName("status")
    private String status;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("annualTurnover")
    private double annualTurnover;

    @SerializedName("vatRegistered")
    private boolean vatRegistered;

    @SerializedName("binNo")
    private String binNo;

    @SerializedName("vatStatus")
    private String vatStatus;

    @SerializedName("districtName")
    private String districtName;

    @SerializedName("divisionName")
    private String divisionName;

    public long   getId()                  { return id; }
    public String getBusinessName()        { return businessName; }
    public String getBusinessTypeName()    { return businessTypeName; }
    public String getBusinessCategoryName(){ return businessCategoryName; }
    public String getStatus()              { return status; }
    public String getTinNumber()           { return tinNumber; }
    public String getPhone()               { return phone; }
    public String getAddress()             { return address; }
    public double getAnnualTurnover()      { return annualTurnover; }
    public boolean isVatRegistered()       { return vatRegistered; }
    public String getBinNo()               { return binNo; }
    public String getVatStatus()           { return vatStatus; }
    public String getDistrictName()        { return districtName; }
    public String getDivisionName()        { return divisionName; }
}