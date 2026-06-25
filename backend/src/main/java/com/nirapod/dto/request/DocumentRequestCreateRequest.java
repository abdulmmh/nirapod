package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class DocumentRequestCreateRequest {

  @NotBlank(message = "Requested documents is required")
  private String requestedDocuments;

  private String requestReason;
  private String requestType = "INITIAL";
  private LocalDate deadline;

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

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
  }
}