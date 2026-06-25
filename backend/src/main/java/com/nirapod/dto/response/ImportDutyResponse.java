package com.nirapod.dto.response;

import com.nirapod.model.ImportDuty;
import java.math.BigDecimal;

public class ImportDutyResponse {
	
  public Long id;
  public String dutyRef;
  public String tinNumber;
  public String taxpayerName;
  public String businessName;
  public Long productId;
  public String productName;
  public String hsCode;
  public String goodsDescription;
  public String originCountry;
  public BigDecimal cifValue;
  public BigDecimal cdRate;
  public BigDecimal sdRate;
  public BigDecimal vatRate;
  public BigDecimal aitRate;
  public BigDecimal atRate;
  public BigDecimal customsDuty;
  public BigDecimal supplementaryDuty;
  public BigDecimal vat;
  public BigDecimal advanceIncomeTax;
  public BigDecimal advanceTax;
  public BigDecimal totalPayable;
  public BigDecimal paidAmount;
  public String portOfEntry;
  public String boeNumber;
  public String boeDate;
  public String billOfLading;
  public String importDate;
  public String assessmentDate;
  public String status;
  public String remarks;

  public static ImportDutyResponse from(ImportDuty e) {
    ImportDutyResponse r = new ImportDutyResponse();
    r.id = e.getId();
    r.dutyRef = e.getDutyRef();
    r.tinNumber = e.getTinNumber();
    r.taxpayerName = e.getTaxpayerName();
    r.businessName = e.getBusinessName();
    r.productId = e.getProductId();
    r.productName = e.getProductName();
    r.hsCode = e.getHsCode();
    r.goodsDescription = e.getGoodsDescription();
    r.originCountry = e.getOriginCountry();
    r.cifValue = e.getCifValue();
    r.cdRate = e.getCdRate();
    r.sdRate = e.getSdRate();
    r.vatRate = e.getVatRate();
    r.aitRate = e.getAitRate();
    r.atRate = e.getAtRate();
    r.customsDuty = e.getCustomsDuty();
    r.supplementaryDuty = e.getSupplementaryDuty();
    r.vat = e.getVat();
    r.advanceIncomeTax = e.getAdvanceIncomeTax();
    r.advanceTax = e.getAdvanceTax();
    r.totalPayable = e.getTotalPayable();
    r.paidAmount = e.getPaidAmount();
    r.portOfEntry = e.getPortOfEntry();
    r.boeNumber = e.getBoeNumber();
    r.boeDate = e.getBoeDate();
    r.billOfLading = e.getBillOfLading();
    r.importDate = e.getImportDate();
    r.assessmentDate = e.getAssessmentDate();
    r.status = e.getStatus();
    r.remarks = e.getRemarks();
    return r;
  }
}

