package com.nirapod.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "aitStatusHistory")
@Table(name = "ait_status_history", indexes = {
    @Index(name = "idx_ait_status_history_ait_record_id", columnList = "ait_record_id"),
    @Index(name = "idx_ait_status_history_changed_at", columnList = "changed_at")
})
public class AitStatusHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ait_record_id", nullable = false)
  private AitRecord aitRecord;

  @Column(name = "from_status", length = 50, nullable = false)
  private String fromStatus;

  @Column(name = "to_status", length = 50, nullable = false)
  private String toStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "changed_by", nullable = false)
  private User changedBy;

  @CreationTimestamp
  @Column(name = "changed_at", nullable = false, updatable = false)
  private LocalDateTime changedAt;

  @Column(name = "change_reason", columnDefinition = "TEXT")
  private String changeReason;

  public AitStatusHistory() {
  }

  public AitStatusHistory(Long id, AitRecord aitRecord, String fromStatus, String toStatus, User changedBy,
      LocalDateTime changedAt, String changeReason) {
    this.id = id;
    this.aitRecord = aitRecord;
    this.fromStatus = fromStatus;
    this.toStatus = toStatus;
    this.changedBy = changedBy;
    this.changedAt = changedAt;
    this.changeReason = changeReason;
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

  public User getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(User changedBy) {
    this.changedBy = changedBy;
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
