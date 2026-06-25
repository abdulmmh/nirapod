package com.nirapod.dto.response;

import com.nirapod.model.AitDocumentRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AitDocumentRequestDto {

  private Long id;
  private String requestType;
  private String requestedDocuments;
  private String requestReason;
  private String requestedBy;
  private LocalDateTime requestedAt;
  private LocalDate deadline;
  private String status;

  public static AitDocumentRequestDto from(AitDocumentRequest entity) {
    AitDocumentRequestDto dto = new AitDocumentRequestDto();
    dto.setId(entity.getId());
    dto.setRequestType(entity.getRequestType());
    dto.setRequestedDocuments(entity.getRequestedDocuments());
    dto.setRequestReason(entity.getRequestReason());
    dto.setRequestedBy(entity.getRequestedBy().getFullName());
    dto.setRequestedAt(entity.getRequestedAt());
    dto.setDeadline(entity.getDeadline());
    dto.setStatus(entity.getStatus());
    return dto;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

}
