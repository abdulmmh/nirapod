package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class Business {

    @SerializedName("id")
    private long id;

    @SerializedName("businessName")
    private String businessName;

    @SerializedName("businessRegNo")
    private String businessRegNo;

    @SerializedName("tinNumber")
    private String tinNumber;

    @SerializedName("ownerName")
    private String ownerName;

    @SerializedName("businessType")
    private Object businessType; // Can be string or object {id, typeName}

    @SerializedName("businessCategory")
    private Object businessCategory; // Can be string or object {id, categoryName}

    @SerializedName("tradeLicenseNo")
    private String tradeLicenseNo;

    @SerializedName("binNo")
    private String binNo;

    @SerializedName("incorporationDate")
    private String incorporationDate;

    @SerializedName("registrationDate")
    private String registrationDate;

    @SerializedName("expiryDate")
    private String expiryDate;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("status")
    private String status;

    @SerializedName("division")
    private Object division; // {id, name}

    @SerializedName("district")
    private Object district; // {id, name}

    @SerializedName("address")
    private String address;

    @SerializedName("annualTurnover")
    private double annualTurnover;

    @SerializedName("numberOfEmployees")
    private Integer numberOfEmployees;

    @SerializedName("remarks")
    private String remarks;

    public long   getId()                  { return id; }
    public String getBusinessName()        { return businessName; }
    public String getBusinessRegNo()       { return businessRegNo; }
    public String getTinNumber()           { return tinNumber; }
    public String getOwnerName()           { return ownerName; }
    public String getTradeLicenseNo()      { return tradeLicenseNo; }
    public String getBinNo()               { return binNo; }
    public String getIncorporationDate()   { return incorporationDate; }
    public String getRegistrationDate()    { return registrationDate; }
    public String getExpiryDate()          { return expiryDate; }
    public String getEmail()               { return email; }
    public String getPhone()               { return phone; }
    public String getStatus()              { return status; }
    public String getAddress()             { return address; }
    public double getAnnualTurnover()      { return annualTurnover; }
    public Integer getNumberOfEmployees()  { return numberOfEmployees; }
    public String getRemarks()             { return remarks; }

    // Helper to extract names from objects or strings
    public String getBusinessTypeName() {
        return extractName(businessType, "typeName");
    }

    public String getBusinessCategoryName() {
        return extractName(businessCategory, "categoryName");
    }

    public String getDivisionName() {
        return extractName(division, "name");
    }

    public String getDistrictName() {
        return extractName(district, "name");
    }

    private String extractName(Object obj, String field) {
        if (obj == null) return "—";
        if (obj instanceof String) return (String) obj;
        if (obj instanceof java.util.Map) {
            Object val = ((java.util.Map<?, ?>) obj).get(field);
            return val != null ? val.toString() : "—";
        }
        return "—";
    }
}
