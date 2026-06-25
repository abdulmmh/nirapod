package com.nirapod.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "aitDocumentRequest")
@Table(name = "ait_document_requests", indexes = {
    @Index(name = "idx_ait_document_requests_ait_record_id", columnList = "ait_record_id"),
    @Index(name = "idx_ait_document_requests_status", columnList = "status")
})
public class AitDocumentRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ait_record_id", nullable = false)
  private AitRecord aitRecord;

  @Column(name = "request_type", length = 50, nullable = false)
  private String requestType;

  @Column(name = "requested_documents", columnDefinition = "TEXT")
  private String requestedDocuments;

  @Column(name = "request_reason", columnDefinition = "TEXT")
  private String requestReason;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "requested_by", nullable = false)
  private User requestedBy;

  @CreationTimestamp
  @Column(name = "requested_at", nullable = false, updatable = false)
  private LocalDateTime requestedAt;

  @Column(name = "deadline")
  private LocalDate deadline;

  @Column(name = "status", length = 50, nullable = false)
  private String status = "PENDING";

  @Column(name = "is_deleted")
  private Boolean isDeleted = false;

  public AitDocumentRequest() {
  }

  public AitDocumentRequest(Long id, AitRecord aitRecord, String requestType, String requestedDocuments,
      String requestReason, User requestedBy, LocalDateTime requestedAt, LocalDate deadline,
      String status, Boolean isDeleted) {
    this.id = id;
    this.aitRecord = aitRecord;
    this.requestType = requestType;
    this.requestedDocuments = requestedDocuments;
    this.requestReason = requestReason;
    this.requestedBy = requestedBy;
    this.requestedAt = requestedAt;
    this.deadline = deadline;
    this.status = status;
    this.isDeleted = isDeleted;
  }

  @PrePersist
  protected void onCreate() {
    if (status == null) {
      status = "PENDING";
    }
    if (isDeleted == null) {
      isDeleted = false;
    }
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AitRecord getAitRecord() {
    return aitRecord;
  }

  public void setAitRecord(AitRecord aitRecord) {
    this.aitRecord = aitRecord;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getRequestedDocuments() {
    return requestedDocuments;
  }

  public void setRequestedDocuments(String requestedDocuments) {
    this.requestedDocuments = requestedDocuments;
  }

  public String getRequestReason() {
    return requestReason;
  }

  public void setRequestReason(String requestReason) {
    this.requestReason = requestReason;
  }

  public User getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(User requestedBy) {
    this.requestedBy = requestedBy;
  }

  public LocalDateTime getRequestedAt() {
    return requestedAt;
  }

  public void setRequestedAt(LocalDateTime requestedAt) {
    this.requestedAt = requestedAt;
  }

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
