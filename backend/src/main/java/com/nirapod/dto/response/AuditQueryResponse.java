package com.nirapod.dto.response;

import com.nirapod.model.AuditQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuditQueryResponse {

  private Long id;
  private Long auditCaseId;
  private String queryNo;
  private String subject;
  private String queryText;
  private String queryType;
  private String raisedBy;
  private LocalDateTime raisedAt;
  private String responseText;
  private String respondedBy;
  private LocalDateTime respondedAt;
  private LocalDate deadline;
  private String status;

  public static AuditQueryResponse from(AuditQuery q) {
    AuditQueryResponse r = new AuditQueryResponse();
    r.id = q.getId();
    r.auditCaseId = q.getAuditCase() != null ? q.getAuditCase().getId() : null;
    r.queryNo = q.getQueryNo();
    r.subject = q.getSubject();
    r.queryText = q.getQueryText();
    r.queryType = q.getQueryType();
    r.raisedBy = q.getRaisedBy();
    r.raisedAt = q.getRaisedAt();
    r.responseText = q.getResponseText();
    r.respondedBy = q.getRespondedBy();
    r.respondedAt = q.getRespondedAt();
    r.deadline = q.getDeadline();
    r.status = q.getStatus();
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
}