package com.nirapod.dto.response;

public class RegistrationResponse {

  private Integer userId;
  private Long taxpayerId;
  private String tinNumber;
  private String fullName;
  private String email;
  private String accountCategory;
  private String taxpayerTypeName;
  private String message;

  public RegistrationResponse(Long long1, Long long2, String generatedTin, String string, String string2,
      String string3, String string4, String string5) {
  }

  public RegistrationResponse(
      Integer userId,
      Long taxpayerId,
      String tinNumber,
      String fullName,
      String email,
      String accountCategory,
      String taxpayerTypeName,
      String message) {
    this.userId = userId;
    this.taxpayerId = taxpayerId;
    this.tinNumber = tinNumber;
    this.fullName = fullName;
    this.email = email;
    this.accountCategory = accountCategory;
    this.taxpayerTypeName = taxpayerTypeName;
    this.message = message;
  }

  public Integer getUserId() {
    return userId;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public String getAccountCategory() {
    return accountCategory;
  }

  public String getTaxpayerTypeName() {
    return taxpayerTypeName;
  }

  public String getMessage() {
    return message;
  }
}
