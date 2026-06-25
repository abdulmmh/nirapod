package com.nirapod.dto.response;

import com.nirapod.model.AitRecord;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AitResponse {

  private Long id;
  private String aitReferenceNo;

  // Taxpayer
  private Long taxpayerId;
  private String taxpayerName;
  private String taxpayerTin;

  // Fiscal year
  private Long fiscalYearId;
  private String fiscalYearName;

  // Source
  private String sourceType;
  private Long importDutyRecordId;
  private String hsCode;
  private String deductorName;
  private String deductorTin;

  // Financials
  private BigDecimal taxableValue;
  private BigDecimal aitRate;
  private BigDecimal calculatedAitAmount;
  private BigDecimal approvedAitAmount;

  // Challan
  private String challanNumber;
  private String bankName;
  private Boolean challanVerified;

  // Workflow
  private String status;
  private Long assignedOfficerId;
  private String assignedOfficerName;
  private String rejectionReason;
  private String approvalNotes;

  // Timestamps
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;


  public static AitResponse from(AitRecord r) {
    AitResponse dto = new AitResponse();
    dto.id = r.getId();
    dto.aitReferenceNo = r.getAitReferenceNo();

    if (r.getTaxpayer() != null) {
      dto.taxpayerId = r.getTaxpayer().getId();
      String name = r.getTaxpayer().getFullName();
      if (name == null || name.isBlank())
        name = r.getTaxpayer().getCompanyName();
      dto.taxpayerName = name;
      dto.taxpayerTin = r.getTaxpayer().getTinNumber();
    }

    if (r.getFiscalYear() != null) {
      dto.fiscalYearId = r.getFiscalYear().getId();
      dto.fiscalYearName = r.getFiscalYear().getYearName();
    }

    dto.sourceType = r.getSourceType();
    dto.importDutyRecordId = r.getImportDutyRecordId();
    dto.hsCode = r.getHsCode();
    dto.deductorName = r.getDeductorName();
    dto.deductorTin = r.getDeductorTin();

    dto.taxableValue = r.getTaxableValue();
    dto.aitRate = r.getAitRate();
    dto.calculatedAitAmount = r.getCalculatedAitAmount();
    dto.approvedAitAmount = r.getApprovedAitAmount();

    dto.challanNumber = r.getChallanNumber();
    dto.bankName = r.getBankName();
    dto.challanVerified = r.getChallanVerified();

    dto.status = r.getStatus();
    dto.rejectionReason = r.getRejectionReason();
    dto.approvalNotes = r.getApprovalNotes();

    if (r.getAssignedOfficer() != null) {
      dto.assignedOfficerId = r.getAssignedOfficer().getId();
      dto.assignedOfficerName = r.getAssignedOfficer().getFullName() != null
          ? r.getAssignedOfficer().getFullName()
          : r.getAssignedOfficer().getEmail();
    }

    dto.createdAt = r.getCreatedAt();
    dto.updatedAt = r.getUpdatedAt();

    return dto;
  }


  public static class Detail extends AitResponse {

    private List<AitStatusHistoryResponse> statusHistory;
    private List<AitDocumentDto> documents;
    private List<AitDocumentRequestDto> pendingRequests;

    public static Detail from(AitRecord r) {
      Detail dto = new Detail();

      // Copy base fields
      AitResponse base = AitResponse.from(r);
      dto.setId(base.getId());
      dto.setAitReferenceNo(base.getAitReferenceNo());
      dto.setTaxpayerId(base.getTaxpayerId());
      dto.setTaxpayerName(base.getTaxpayerName());
      dto.setTaxpayerTin(base.getTaxpayerTin());
      dto.setFiscalYearId(base.getFiscalYearId());
      dto.setFiscalYearName(base.getFiscalYearName());
      dto.setSourceType(base.getSourceType());
      dto.setImportDutyRecordId(base.getImportDutyRecordId());
      dto.setHsCode(base.getHsCode());
      dto.setDeductorName(base.getDeductorName());
      dto.setDeductorTin(base.getDeductorTin());
      dto.setTaxableValue(base.getTaxableValue());
      dto.setAitRate(base.getAitRate());
      dto.setCalculatedAitAmount(base.getCalculatedAitAmount());
      dto.setApprovedAitAmount(base.getApprovedAitAmount());
      dto.setChallanNumber(base.getChallanNumber());
      dto.setBankName(base.getBankName());
      dto.setChallanVerified(base.getChallanVerified());
      dto.setStatus(base.getStatus());
      dto.setAssignedOfficerId(base.getAssignedOfficerId());
      dto.setAssignedOfficerName(base.getAssignedOfficerName());
      dto.setRejectionReason(base.getRejectionReason());
      dto.setApprovalNotes(base.getApprovalNotes());
      dto.setCreatedAt(base.getCreatedAt());
      dto.setUpdatedAt(base.getUpdatedAt());

      // Enrich
      dto.statusHistory = r.getStatusHistory().stream()
          .map(AitStatusHistoryResponse::from)
          .collect(Collectors.toList());

      dto.documents = r.getDocuments().stream()
          .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
          .map(AitDocumentDto::from)
          .collect(Collectors.toList());

      dto.pendingRequests = r.getDocumentRequests().stream()
          .filter(req -> "PENDING".equals(req.getStatus())
              && !Boolean.TRUE.equals(req.getIsDeleted()))
          .map(AitDocumentRequestDto::from)
          .collect(Collectors.toList());

      return dto;
    }

    public List<AitStatusHistoryResponse> getStatusHistory() {
      return statusHistory;
    }

    public void setStatusHistory(List<AitStatusHistoryResponse> h) {
      this.statusHistory = h;
    }

    public List<AitDocumentDto> getDocuments() {
      return documents;
    }

    public void setDocuments(List<AitDocumentDto> d) {
      this.documents = d;
    }

    public List<AitDocumentRequestDto> getPendingRequests() {
      return pendingRequests;
    }

    public void setPendingRequests(List<AitDocumentRequestDto> r) {
      this.pendingRequests = r;
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAitReferenceNo() {
    return aitReferenceNo;
  }

  public void setAitReferenceNo(String v) {
    this.aitReferenceNo = v;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public void setTaxpayerId(Long v) {
    this.taxpayerId = v;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public void setTaxpayerName(String v) {
    this.taxpayerName = v;
  }

  public String getTaxpayerTin() {
    return taxpayerTin;
  }

  public void setTaxpayerTin(String v) {
    this.taxpayerTin = v;
  }

  public Long getFiscalYearId() {
    return fiscalYearId;
  }

  public void setFiscalYearId(Long v) {
    this.fiscalYearId = v;
  }

  public String getFiscalYearName() {
    return fiscalYearName;
  }

  public void setFiscalYearName(String v) {
    this.fiscalYearName = v;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String v) {
    this.sourceType = v;
  }

  public Long getImportDutyRecordId() {
    return importDutyRecordId;
  }

  public void setImportDutyRecordId(Long v) {
    this.importDutyRecordId = v;
  }

  public String getHsCode() {
    return hsCode;
  }

  public void setHsCode(String v) {
    this.hsCode = v;
  }

  public String getDeductorName() {
    return deductorName;
  }

  public void setDeductorName(String v) {
    this.deductorName = v;
  }

  public String getDeductorTin() {
    return deductorTin;
  }

  public void setDeductorTin(String v) {
    this.deductorTin = v;
  }

  public BigDecimal getTaxableValue() {
    return taxableValue;
  }

  public void setTaxableValue(BigDecimal v) {
    this.taxableValue = v;
  }

  public BigDecimal getAitRate() {
    return aitRate;
  }

  public void setAitRate(BigDecimal v) {
    this.aitRate = v;
  }

  public BigDecimal getCalculatedAitAmount() {
    return calculatedAitAmount;
  }

  public void setCalculatedAitAmount(BigDecimal v) {
    this.calculatedAitAmount = v;
  }

  public BigDecimal getApprovedAitAmount() {
    return approvedAitAmount;
  }

  public void setApprovedAitAmount(BigDecimal v) {
    this.approvedAitAmount = v;
  }

  public String getChallanNumber() {
    return challanNumber;
  }

  public void setChallanNumber(String v) {
    this.challanNumber = v;
  }

  public String getBankName() {
    return bankName;
  }

  public void setBankName(String v) {
    this.bankName = v;
  }

  public Boolean getChallanVerified() {
    return challanVerified;
  }

  public void setChallanVerified(Boolean v) {
    this.challanVerified = v;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String v) {
    this.status = v;
  }

  public Long getAssignedOfficerId() {
    return assignedOfficerId;
  }

  public void setAssignedOfficerId(Long v) {
    this.assignedOfficerId = v;
  }

  public String getAssignedOfficerName() {
    return assignedOfficerName;
  }

  public void setAssignedOfficerName(String v) {
    this.assignedOfficerName = v;
  }

  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String v) {
    this.rejectionReason = v;
  }

  public String getApprovalNotes() {
    return approvalNotes;
  }

  public void setApprovalNotes(String v) {
    this.approvalNotes = v;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime v) {
    this.createdAt = v;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime v) {
    this.updatedAt = v;
  }
}
