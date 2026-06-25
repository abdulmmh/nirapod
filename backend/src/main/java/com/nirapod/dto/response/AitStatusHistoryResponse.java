package com.nirapod.dto.response;

import com.nirapod.model.AitStatusHistory;

import java.time.LocalDateTime;

public class AitStatusHistoryResponse {

  private Long id;
  private String fromStatus;
  private String toStatus;
  private String changedBy;
  private LocalDateTime changedAt;
  private String changeReason;

  public static AitStatusHistoryResponse from(AitStatusHistory entity) {
    AitStatusHistoryResponse dto = new AitStatusHistoryResponse();
    dto.setId(entity.getId());
    dto.setFromStatus(entity.getFromStatus());
    dto.setToStatus(entity.getToStatus());
    dto.setChangedBy(entity.getChangedBy().getFullName());
    dto.setChangedAt(entity.getChangedAt());
    dto.setChangeReason(entity.getChangeReason());
    return dto;
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
