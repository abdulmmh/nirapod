package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class AuditQueryCreateRequest {

  @NotBlank(message = "Subject is required")
  private String subject;

  @NotBlank(message = "Query text is required")
  private String queryText;

  private String queryType = "GENERAL";
  private LocalDate deadline;

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

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
  }
}