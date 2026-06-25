// â”€â”€â”€ AuditQuery.java â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_queries")
public class AuditQuery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "audit_case_id", nullable = false)
  private AuditCase auditCase;

  @Column(name = "query_no", nullable = false, unique = true, length = 50)
  private String queryNo;

  @Column(name = "subject", nullable = false, length = 500)
  private String subject;

  @Column(name = "query_text", nullable = false, columnDefinition = "TEXT")
  private String queryText;

  @Column(name = "query_type", length = 50)
  private String queryType = "GENERAL";

  @Column(name = "raised_by", length = 200)
  private String raisedBy;

  @Column(name = "raised_at", nullable = false)
  private LocalDateTime raisedAt;

  @Column(name = "response_text", columnDefinition = "TEXT")
  private String responseText;

  @Column(name = "responded_by", length = 200)
  private String respondedBy;

  @Column(name = "responded_at")
  private LocalDateTime respondedAt;

  @Column(name = "deadline")
  private LocalDate deadline;

  @Column(name = "status", nullable = false, length = 30)
  private String status = "OPEN";

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
    this.raisedAt = LocalDateTime.now();
    if (this.queryNo == null) {
      this.queryNo = "QRY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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

  public String getQueryNo() {
    return queryNo;
  }

  public void setQueryNo(String queryNo) {
    this.queryNo = queryNo;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getQueryText() {
    return queryText;
  }

  public void setQueryText(String queryText) {
    this.queryText = queryText;
  }

  public String getQueryType() {
    return queryType;
  }

  public void setQueryType(String queryType) {
    this.queryType = queryType;
  }

  public String getRaisedBy() {
    return raisedBy;
  }

  public void setRaisedBy(String raisedBy) {
    this.raisedBy = raisedBy;
  }

  public LocalDateTime getRaisedAt() {
    return raisedAt;
  }

  public void setRaisedAt(LocalDateTime raisedAt) {
    this.raisedAt = raisedAt;
  }

  public String getResponseText() {
    return responseText;
  }

  public void setResponseText(String responseText) {
    this.responseText = responseText;
  }

  public String getRespondedBy() {
    return respondedBy;
  }

  public void setRespondedBy(String respondedBy) {
    this.respondedBy = respondedBy;
  }

  public LocalDateTime getRespondedAt() {
    return respondedAt;
  }

  public void setRespondedAt(LocalDateTime respondedAt) {
    this.respondedAt = respondedAt;
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
