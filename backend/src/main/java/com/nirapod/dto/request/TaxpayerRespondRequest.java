package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TaxpayerRespondRequest {

  @NotBlank(message = "Response text is required")
  private String responseText;

  private Long queryId;
  private Long documentRequestId;

  public String getResponseText() {
    return responseText;
  }

  public void setResponseText(String responseText) {
    this.responseText = responseText;
  }

  public Long getQueryId() {
    return queryId;
  }

  public void setQueryId(Long queryId) {
    this.queryId = queryId;
  }

  public Long getDocumentRequestId() {
    return documentRequestId;
  }

  public void setDocumentRequestId(Long documentRequestId) {
    this.documentRequestId = documentRequestId;
  }
}