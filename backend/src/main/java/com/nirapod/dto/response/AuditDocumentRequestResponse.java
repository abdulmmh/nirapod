package com.nirapod.dto.response;

import com.nirapod.dto.request.AuditDocumentRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuditDocumentRequestResponse {

  private Long id;
  private Long auditCaseId;
  private String requestNo;
  private String requestedDocuments;
  private String requestReason;
  private String requestType;
  private String requestedBy;
  private LocalDateTime requestedAt;
  private LocalDate deadline;
  private String status;
  private String fulfillmentNotes;

  public static AuditDocumentRequestResponse from(AuditDocumentRequest d) {
    AuditDocumentRequestResponse r = new AuditDocumentRequestResponse();
    r.id = d.getId();
    r.auditCaseId = d.getAuditCase() != null ? d.getAuditCase().getId() : null;
    r.requestNo = d.getRequestNo();
    r.requestedDocuments = d.getRequestedDocuments();
    r.requestReason = d.getRequestReason();
    r.requestType = d.getRequestType();
    r.requestedBy = d.getRequestedBy();
    r.requestedAt = d.getRequestedAt();
    r.deadline = d.getDeadline();
    r.status = d.getStatus();
    r.fulfillmentNotes = d.getFulfillmentNotes();
    return r;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAuditCaseId() {
    return auditCaseId;
  }

  public void setAuditCaseId(Long auditCaseId) {
    this.auditCaseId = auditCaseId;
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
}