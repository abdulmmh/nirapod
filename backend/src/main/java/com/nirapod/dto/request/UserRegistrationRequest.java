package com.nirapod.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationRequest {

  // â”€â”€ Common â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  @NotNull(message = "Taxpayer type is required.")
  private Long taxpayerTypeId;

  @NotBlank(message = "Account category is required.")
  private String accountCategory;

  @NotBlank(message = "Full name is required.")
  @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
  private String fullName;

  @NotBlank(message = "Email is required.")
  @Email(message = "Please provide a valid email address.")
  private String email;

  @NotBlank(message = "Phone number is required.")
  @Pattern(regexp = "^01[3-9]\\d{8}$", message = "Phone must be a valid Bangladeshi number (e.g. 01712345678).")
  private String phone;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be at least 8 characters.")
  private String password;

  private String existingTin;

  private String nid;
  private String dateOfBirth;
  private String gender;
  private String profession;

  private String companyName;
  private String rjscNo;
  private String incorporationDate;
  private String natureOfBusiness;
  private String authorizedPersonName;
  private String authorizedPersonNid;

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public String getExistingTin() {
    return existingTin;
  }

  public void setExistingTin(String v) {
    this.existingTin = v;
  }

  public String getFullName() {
    return fullName;
  }

  public Long getTaxpayerTypeId() {
    return taxpayerTypeId;
  }

  public void setTaxpayerTypeId(Long taxpayerTypeId) {
    this.taxpayerTypeId = taxpayerTypeId;
  }

  public String getAccountCategory() {
    return accountCategory;
  }

  public void setAccountCategory(String accountCategory) {
    this.accountCategory = accountCategory;
  }

  public void setFullName(String v) {
    this.fullName = v;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String v) {
    this.email = v;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String v) {
    this.phone = v;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String v) {
    this.password = v;
  }

  public String getNid() {
    return nid;
  }

  public void setNid(String v) {
    this.nid = v;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String v) {
    this.dateOfBirth = v;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String v) {
    this.gender = v;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String v) {
    this.profession = v;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String v) {
    this.companyName = v;
  }

  public String getRjscNo() {
    return rjscNo;
  }

  public void setRjscNo(String v) {
    this.rjscNo = v;
  }

  public String getIncorporationDate() {
    return incorporationDate;
  }

  public void setIncorporationDate(String v) {
    this.incorporationDate = v;
  }

  public String getNatureOfBusiness() {
    return natureOfBusiness;
  }

  public void setNatureOfBusiness(String v) {
    this.natureOfBusiness = v;
  }

  public String getAuthorizedPersonName() {
    return authorizedPersonName;
  }

  public void setAuthorizedPersonName(String v) {
    this.authorizedPersonName = v;
  }

  public String getAuthorizedPersonNid() {
    return authorizedPersonNid;
  }

  public void setAuthorizedPersonNid(String v) {
    this.authorizedPersonNid = v;
  }
}
