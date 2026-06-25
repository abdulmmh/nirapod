package com.nirapod.dto.response;

import com.nirapod.model.AitRecord;

import java.util.List;
import java.util.stream.Collectors;

public class AitDetailResponse extends AitResponse {
  private List<AitStatusHistoryResponse> statusHistory;
  private List<AitDocumentDto> documents;
  private List<AitDocumentRequestDto> pendingRequests;

  public static AitDetailResponse from(AitRecord entity) {
    AitDetailResponse response = new AitDetailResponse();
    AitResponse base = AitResponse.from(entity);
    response.setId(base.getId());
    response.setAitReferenceNo(base.getAitReferenceNo());
    response.setTaxpayerId(base.getTaxpayerId());
    response.setTaxpayerName(base.getTaxpayerName());
    response.setImportDutyRecordId(base.getImportDutyRecordId());
    response.setHsCode(base.getHsCode());
    response.setTaxableValue(base.getTaxableValue());
    response.setAitRate(base.getAitRate());
    response.setCalculatedAitAmount(base.getCalculatedAitAmount());
    response.setApprovedAitAmount(base.getApprovedAitAmount());
    response.setStatus(base.getStatus());
    response.setAssignedOfficerId(base.getAssignedOfficerId());
    response.setAssignedOfficerName(base.getAssignedOfficerName());
    response.setCreatedAt(base.getCreatedAt());
    response.setUpdatedAt(base.getUpdatedAt());

    response.setStatusHistory(entity.getStatusHistory().stream()
        .map(AitStatusHistoryResponse::from)
        .collect(Collectors.toList()));
    response.setDocuments(entity.getDocuments().stream()
        .filter(d -> !d.getIsDeleted())
        .map(AitDocumentDto::from)
        .collect(Collectors.toList()));
    response.setPendingRequests(entity.getDocumentRequests().stream()
        .filter(r -> "PENDING".equals(r.getStatus()) && !r.getIsDeleted())
        .map(AitDocumentRequestDto::from)
        .collect(Collectors.toList()));

    return response;
  }

  public List<AitStatusHistoryResponse> getStatusHistory() {
    return statusHistory;
  }

  public void setStatusHistory(List<AitStatusHistoryResponse> statusHistory) {
    this.statusHistory = statusHistory;
  }

  public List<AitDocumentDto> getDocuments() {
    return documents;
  }

  public void setDocuments(List<AitDocumentDto> documents) {
    this.documents = documents;
  }

  public List<AitDocumentRequestDto> getPendingRequests() {
    return pendingRequests;
  }

  public void setPendingRequests(List<AitDocumentRequestDto> pendingRequests) {
    this.pendingRequests = pendingRequests;
  }

}
