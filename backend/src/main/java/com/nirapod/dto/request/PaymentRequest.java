package com.nirapod.dto.request;

public class PaymentRequest {
  private Long paymentId;
  private String mockStatus;
  private Double amount;
  private String tinNumber;

  // Getters and Setters
  public Long getPaymentId() {
    return paymentId;
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

  public String getMockStatus() {
    return mockStatus;
  }

  public void setMockStatus(String mockStatus) {
    this.mockStatus = mockStatus;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }
}