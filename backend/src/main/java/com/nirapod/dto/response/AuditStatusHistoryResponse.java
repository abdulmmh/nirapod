package com.nirapod.dto.response;

import com.nirapod.model.AuditStatusHistory;
import java.time.LocalDateTime;

public class AuditStatusHistoryResponse {

  private Long id;
  private String fromStatus;
  private String toStatus;
  private String changedBy;
  private String changedByRole;
  private LocalDateTime changedAt;
  private String changeReason;

  public static AuditStatusHistoryResponse from(AuditStatusHistory h) {
    AuditStatusHistoryResponse r = new AuditStatusHistoryResponse();
    r.id = h.getId();
    r.fromStatus = h.getFromStatus();
    r.toStatus = h.getToStatus();
    r.changedBy = h.getChangedBy();
    r.changedByRole = h.getChangedByRole();
    r.changedAt = h.getChangedAt();
    r.changeReason = h.getChangeReason();
    return r;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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