package com.nirapod.dto.request;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nirapod.model.AuditCase;

@Entity
@Table(name = "audit_document_requests")
public class AuditDocumentRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "audit_case_id", nullable = false)
  private AuditCase auditCase;

  @Column(name = "request_no", nullable = false, unique = true, length = 50)
  private String requestNo;

  @Column(name = "requested_documents", nullable = false, columnDefinition = "TEXT")
  private String requestedDocuments;

  @Column(name = "request_reason", columnDefinition = "TEXT")
  private String requestReason;

  @Column(name = "request_type", length = 50)
  private String requestType = "INITIAL";

  @Column(name = "requested_by", length = 200)
  private String requestedBy;

  @Column(name = "requested_at", nullable = false)
  private LocalDateTime requestedAt;

  @Column(name = "deadline")
  private LocalDate deadline;

  @Column(name = "status", nullable = false, length = 30)
  private String status = "PENDING";

  @Column(name = "fulfillment_notes", columnDefinition = "TEXT")
  private String fulfillmentNotes;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted = false;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
    this.requestedAt = LocalDateTime.now();
    if (this.requestNo == null) {
      this.requestNo = "DOCREQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  // Getters & Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AuditCase getAuditCase() {
    return auditCase;
  }

  public void setAuditCase(AuditCase auditCase) {
    this.auditCase = auditCase;
  }

  public String getRequestNo() {
    return requestNo;
  }

  public void setRequestNo(String requestNo) {
    this.requestNo = requestNo;
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

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(String requestedBy) {
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

  public String getFulfillmentNotes() {
    return fulfillmentNotes;
  }

  public void setFulfillmentNotes(String fulfillmentNotes) {
    this.fulfillmentNotes = fulfillmentNotes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
