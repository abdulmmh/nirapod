package com.nirapod.dto.request;

public class TaxpayerCreateRequest {
	
	private Long taxpayerTypeId;
    private String fullName;
    private String email;
    private String phone;
    private String nid;
    private String gender;
    private String profession;
    private String dateOfBirth;
    private String companyName;
    private String rjscNo;
    
    
    //Getter - Setter
	public Long getTaxpayerTypeId() {
		return taxpayerTypeId;
	}
	public void setTaxpayerTypeId(Long taxpayerTypeId) {
		this.taxpayerTypeId = taxpayerTypeId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRjscNo() {
		return rjscNo;
	}
	public void setRjscNo(String rjscNo) {
		this.rjscNo = rjscNo;
	}
    
    
}
