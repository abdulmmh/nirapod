package com.nirapod.dto.response;

import com.nirapod.model.AitCreditApplication;
import com.nirapod.model.AitCreditLedger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AitCreditLedgerResponse {

  private Long id;
  private Long taxpayerId;
  private String taxpayerName;
  private String taxpayerTin;
  private Long fiscalYearId;
  private String fiscalYearName;
  private Long aitRecordId;
  private String aitReferenceNo;
  private String sourceType;

  private BigDecimal creditedAmount;
  private BigDecimal usedAmount;
  private BigDecimal remainingAmount;

  private String status;
  private String creditedBy;
  private LocalDateTime creditedAt;
  private String notes;
  private LocalDateTime createdAt;

  // Applications list (for detail view)
  private List<ApplicationDto> applications;


  public static AitCreditLedgerResponse from(AitCreditLedger l) {
    AitCreditLedgerResponse dto = new AitCreditLedgerResponse();
    dto.id = l.getId();
    dto.creditedAmount = l.getCreditedAmount();
    dto.usedAmount = l.getUsedAmount();
    dto.remainingAmount = l.getRemainingAmount();
    dto.status = l.getStatus();
    dto.creditedBy = l.getCreditedBy();
    dto.creditedAt = l.getCreditedAt();
    dto.notes = l.getNotes();
    dto.createdAt = l.getCreatedAt();

    if (l.getTaxpayer() != null) {
      dto.taxpayerId = l.getTaxpayer().getId();
      String name = l.getTaxpayer().getFullName();
      if (name == null || name.isBlank())
        name = l.getTaxpayer().getCompanyName();
      dto.taxpayerName = name;
      dto.taxpayerTin = l.getTaxpayer().getTinNumber();
    }

    if (l.getFiscalYear() != null) {
      dto.fiscalYearId = l.getFiscalYear().getId();
      dto.fiscalYearName = l.getFiscalYear().getYearName();
    }

    if (l.getAitRecord() != null) {
      dto.aitRecordId = l.getAitRecord().getId();
      dto.aitReferenceNo = l.getAitRecord().getAitReferenceNo();
      dto.sourceType = l.getAitRecord().getSourceType();
    }

    return dto;
  }

  public static AitCreditLedgerResponse withApplications(
      AitCreditLedger l, List<AitCreditApplication> apps) {
    AitCreditLedgerResponse dto = from(l);
    dto.applications = apps.stream()
        .map(ApplicationDto::from)
        .collect(Collectors.toList());
    return dto;
  }


  public static class ApplicationDto {
    private Long id;
    private Long itrId;
    private String itrReturnNo;
    private BigDecimal appliedAmount;
    private String status;
    private String appliedBy;
    private LocalDateTime appliedAt;
    private String reversalReason;

    public static ApplicationDto from(AitCreditApplication a) {
      ApplicationDto dto = new ApplicationDto();
      dto.id = a.getId();
      dto.appliedAmount = a.getAppliedAmount();
      dto.status = a.getStatus();
      dto.appliedBy = a.getAppliedBy();
      dto.appliedAt = a.getAppliedAt();
      dto.reversalReason = a.getReversalReason();
      if (a.getIncomeTaxReturn() != null) {
        dto.itrId = a.getIncomeTaxReturn().getId();
        dto.itrReturnNo = a.getIncomeTaxReturn().getReturnNo();
      }
      return dto;
    }

    public Long getId() {
      return id;
    }

    public Long getItrId() {
      return itrId;
    }

    public String getItrReturnNo() {
      return itrReturnNo;
    }

    public BigDecimal getAppliedAmount() {
      return appliedAmount;
    }

    public String getStatus() {
      return status;
    }

    public String getAppliedBy() {
      return appliedBy;
    }

    public LocalDateTime getAppliedAt() {
      return appliedAt;
    }

    public String getReversalReason() {
      return reversalReason;
    }
  }


  public static class AvailableSummary {
	    private Long ledgerId;
	    private String aitReferenceNo;
	    private String sourceType;
	    private String fiscalYearName;
	    private BigDecimal creditedAmount;   // ✅ ADD
	    private BigDecimal usedAmount;       // ✅ ADD
	    private BigDecimal remainingAmount;
	    private LocalDateTime creditedAt;

	    public static AvailableSummary from(AitCreditLedger l) {
	        AvailableSummary s = new AvailableSummary();
	        s.ledgerId       = l.getId();
	        s.creditedAmount = l.getCreditedAmount();   // ✅ ADD
	        s.usedAmount     = l.getUsedAmount();        // ✅ ADD
	        s.remainingAmount = l.getRemainingAmount();
	        s.creditedAt     = l.getCreditedAt();
	        if (l.getAitRecord() != null) {
	            s.aitReferenceNo = l.getAitRecord().getAitReferenceNo();
	            s.sourceType     = l.getAitRecord().getSourceType();
	        }
	        if (l.getFiscalYear() != null)
	            s.fiscalYearName = l.getFiscalYear().getYearName();
	        return s;
	    }

	    public Long getLedgerId()            { return ledgerId; }
	    public String getAitReferenceNo()    { return aitReferenceNo; }
	    public String getSourceType()        { return sourceType; }
	    public String getFiscalYearName()    { return fiscalYearName; }
	    public BigDecimal getCreditedAmount(){ return creditedAmount; }  
	    public BigDecimal getUsedAmount()    { return usedAmount; }      
	    public BigDecimal getRemainingAmount(){ return remainingAmount; }
	    public LocalDateTime getCreditedAt() { return creditedAt; }
	}


  public Long getId() {
    return id;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public String getTaxpayerTin() {
    return taxpayerTin;
  }

  public Long getFiscalYearId() {
    return fiscalYearId;
  }

  public String getFiscalYearName() {
    return fiscalYearName;
  }

  public Long getAitRecordId() {
    return aitRecordId;
  }

  public String getAitReferenceNo() {
    return aitReferenceNo;
  }

  public String getSourceType() {
    return sourceType;
  }

  public BigDecimal getCreditedAmount() {
    return creditedAmount;
  }

  public BigDecimal getUsedAmount() {
    return usedAmount;
  }

  public BigDecimal getRemainingAmount() {
    return remainingAmount;
  }

  public String getStatus() {
    return status;
  }

  public String getCreditedBy() {
    return creditedBy;
  }

  public LocalDateTime getCreditedAt() {
    return creditedAt;
  }

  public String getNotes() {
    return notes;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public List<ApplicationDto> getApplications() {
    return applications;
  }
}
