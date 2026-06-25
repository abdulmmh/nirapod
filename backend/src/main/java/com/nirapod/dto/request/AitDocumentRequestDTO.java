package com.nirapod.dto.request;

import java.time.LocalDate;

public class AitDocumentRequestDTO {
  private String requestType;
  private String requestedDocuments;
  private String requestReason;
  private LocalDate deadline;

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

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
  }

}
