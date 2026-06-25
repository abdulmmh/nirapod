package com.nirapod.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_status_history")
public class AuditStatusHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "audit_case_id", nullable = false)
  private AuditCase auditCase;

  @Column(name = "from_status", length = 60)
  private String fromStatus;

  @Column(name = "to_status", nullable = false, length = 60)
  private String toStatus;

  @Column(name = "changed_by", length = 200)
  private String changedBy;

  @Column(name = "changed_by_role", length = 100)
  private String changedByRole;

  @Column(name = "changed_at", nullable = false)
  private LocalDateTime changedAt;

  @Column(name = "change_reason", columnDefinition = "TEXT")
  private String changeReason;

  @PrePersist
  protected void onCreate() {
    if (this.changedAt == null)
      this.changedAt = LocalDateTime.now();
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

  public String getFromStatus() {
    return fromStatus;
  }

  public void setFromStatus(String fromStatus) {
    this.fromStatus = fromStatus;
  }

  public String getToStatus() {
    return toStatus;
  }

  public void setToStatus(String toStatus) {
    this.toStatus = toStatus;
  }

  public String getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(String changedBy) {
    this.changedBy = changedBy;
  }

  public String getChangedByRole() {
    return changedByRole;
  }

  public void setChangedByRole(String changedByRole) {
    this.changedByRole = changedByRole;
  }

  public LocalDateTime getChangedAt() {
    return changedAt;
  }

  public void setChangedAt(LocalDateTime changedAt) {
    this.changedAt = changedAt;
  }

  public String getChangeReason() {
    return changeReason;
  }

  public void setChangeReason(String changeReason) {
    this.changeReason = changeReason;
  }
}
