package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "payment")
@Table(name = "payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 30)
  private String transactionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "taxpayer_id")
  @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "businesses" })
  private Taxpayer taxpayer;

  @Transient
  private Long taxpayerId;

  @Column(nullable = false, length = 30)
  private String tinNumber;

  @Column(nullable = false)
  private String taxpayerName;

  @Column(nullable = false)
  private String paymentType;

  @Column(nullable = false)
  private String paymentMethod;

  @Column(nullable = false)
  private Double amount;

  private String bankName;
  private String bankBranch;
  private String accountNo;
  private String chequeNo;

  @Column(nullable = false)
  private LocalDate paymentDate;

  private LocalDate valueDate;
  private String referenceNo;

  private String returnNo;

  @Column(nullable = false)
  private String status = "Pending";

  private String processedBy;

  @Column(length = 1000)
  private String remarks;

  @Column(nullable = false)
  private boolean isDeleted = false;

  @Column(updatable = false)
  private LocalDateTime createdAt;
  
  private LocalDateTime reviewedAt;    
  private LocalDateTime completedAt;   

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
    if (this.status == null)
      this.status = "Pending";
    if (this.transactionId == null || this.transactionId.isBlank()) {
      this.transactionId = "TXN-" + UUID.randomUUID()
          .toString().replace("-", "").substring(0, 12).toUpperCase();
    }
  }
  
  @PostLoad
  private void populateTransientIds() {
      if (this.taxpayer != null) {
          this.taxpayerId = this.taxpayer.getId();
      }
  }
  
  //  Getters & Setters 

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public Taxpayer getTaxpayer() {
    return taxpayer;
  }

  public void setTaxpayer(Taxpayer taxpayer) {
    this.taxpayer = taxpayer;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long taxpayerId) {
    this.taxpayerId = taxpayerId;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public void setTinNumber(String tinNumber) {
    this.tinNumber = tinNumber;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String taxpayerName) {
    this.taxpayerName = taxpayerName;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  public String getBankBranch() {
    return bankBranch;
  }

  public void setBankBranch(String bankBranch) {
    this.bankBranch = bankBranch;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  public String getChequeNo() {
    return chequeNo;
  }

  public void setChequeNo(String chequeNo) {
    this.chequeNo = chequeNo;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  public LocalDate getValueDate() {
    return valueDate;
  }

  public void setValueDate(LocalDate valueDate) {
    this.valueDate = valueDate;
  }

  public String getReferenceNo() {
    return referenceNo;
  }

  public void setReferenceNo(String referenceNo) {
    this.referenceNo = referenceNo;
  }

  public String getReturnNo() {
    return returnNo;
  }

  public void setReturnNo(String returnNo) {
    this.returnNo = returnNo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProcessedBy() {
    return processedBy;
  }

  public void setProcessedBy(String processedBy) {
    this.processedBy = processedBy;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getReviewedAt() {
	return reviewedAt;
  }

  public void setReviewedAt(LocalDateTime reviewedAt) {
	this.reviewedAt = reviewedAt;
  }

  public LocalDateTime getCompletedAt() {
	return completedAt;
  }

  public void setCompletedAt(LocalDateTime completedAt) {
	this.completedAt = completedAt;
  }
}
