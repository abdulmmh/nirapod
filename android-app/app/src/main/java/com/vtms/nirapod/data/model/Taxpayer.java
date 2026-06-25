package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Taxpayer implements Serializable {

    @SerializedName("id")             private long id;
    @SerializedName("tinNumber")      private String tinNumber;
    @SerializedName("fullName")       private String fullName;
    @SerializedName("companyName")    private String companyName;
    @SerializedName("email")          private String email;
    @SerializedName("phone")          private String phone;
    @SerializedName("gender")         private String gender;
    @SerializedName("profession")     private String profession;
    @SerializedName("nid")            private String nid;
    @SerializedName("fathersName")    private String fathersName;
    @SerializedName("mothersName")    private String mothersName;
    @SerializedName("dateOfBirth")    private String dateOfBirth;
    @SerializedName("approvalStatus") private String approvalStatus;
    @SerializedName("status")         private String status;
    @SerializedName("taxpayerType")   private TaxpayerTypeInfo taxpayerType;
    @SerializedName("presentAddress") private AddressInfo presentAddress;
    @SerializedName("permanentAddress") private AddressInfo permanentAddress;
    @SerializedName("registrationDate") private String registrationDate;
    @SerializedName("photoPath")      private String photoPath;

    public static class TaxpayerTypeInfo implements Serializable {
        @SerializedName("id")       private Long id;
        @SerializedName("typeName") private String typeName;
        @SerializedName("category") private String category;
        
        public Long getId()         { return id; }
        public void setId(Long id)  { this.id = id; }
        public String getTypeName() { return typeName; }
        public void setTypeName(String typeName) { this.typeName = typeName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
    }

    public static class AddressInfo implements Serializable {
        @SerializedName("division")    private String division;
        @SerializedName("district")    private String district;
        @SerializedName("thana")       private String thana;
        @SerializedName("roadVillage") private String roadVillage;

        public String getDivision()    { return division; }
        public String getDistrict()    { return district; }
        public String getThana()       { return thana; }
        public String getRoadVillage() { return roadVillage; }

        public void setDivision(String v)    { division = v; }
        public void setDistrict(String v)    { district = v; }
        public void setThana(String v)       { thana = v; }
        public void setRoadVillage(String v) { roadVillage = v; }
    }

    public String getDisplayName() {
        if (fullName != null && !fullName.isEmpty()) return fullName;
        if (companyName != null && !companyName.isEmpty()) return companyName;
        return tinNumber != null ? tinNumber : "---";
    }

    // Getters
    public long   getId()             { return id; }
    public String getTinNumber()      { return tinNumber; }
    public String getFullName()       { return fullName; }
    public String getCompanyName()    { return companyName; }
    public String getEmail()          { return email; }
    public String getPhone()          { return phone; }
    public String getGender()         { return gender; }
    public String getProfession()     { return profession; }
    public String getNid()            { return nid; }
    public String getFathersName()    { return fathersName; }
    public String getMothersName()    { return mothersName; }
    public String getDateOfBirth()    { return dateOfBirth; }
    public String getApprovalStatus() { return approvalStatus; }
    public String getStatus()         { return status; }
    public TaxpayerTypeInfo getTaxpayerType()   { return taxpayerType; }
    public AddressInfo getPresentAddress()       { return presentAddress; }
    public AddressInfo getPermanentAddress()     { return permanentAddress; }
    public String getRegistrationDate()          { return registrationDate; }
    public String getPhotoPath()                 { return photoPath; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setTinNumber(String tinNumber) { this.tinNumber = tinNumber; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setGender(String gender) { this.gender = gender; }
    public void setProfession(String profession) { this.profession = profession; }
    public void setNid(String nid) { this.nid = nid; }
    public void setFathersName(String fathersName) { this.fathersName = fathersName; }
    public void setMothersName(String mothersName) { this.mothersName = mothersName; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public void setStatus(String status) { this.status = status; }
    public void setTaxpayerType(TaxpayerTypeInfo taxpayerType) { this.taxpayerType = taxpayerType; }
    public void setPresentAddress(AddressInfo presentAddress) { this.presentAddress = presentAddress; }
    public void setPermanentAddress(AddressInfo permanentAddress) { this.permanentAddress = permanentAddress; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
