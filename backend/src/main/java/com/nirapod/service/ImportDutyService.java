package com.nirapod.service;

import com.nirapod.dao.ImportDutyDAO;
import com.nirapod.dto.request.ImportDutyCreateRequest;
import com.nirapod.dto.request.ImportDutyUpdateRequest;
import com.nirapod.dto.response.ImportDutyResponse;
import com.nirapod.dto.response.ImportDutyTaxPreview;
import com.nirapod.model.ImportDuty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImportDutyService {

  @Autowired
  private ImportDutyDAO importDutyDAO;

  public ImportDutyResponse create(ImportDutyCreateRequest req) {
    ImportDuty e = new ImportDuty();
    e.setTinNumber(req.tinNumber);
    e.setTaxpayerName(req.taxpayerName);
    e.setBusinessName(req.businessName);
    e.setProductId(req.productId);
    e.setGoodsDescription(req.goodsDescription);
    e.setOriginCountry(req.originCountry);
    e.setCifValue(req.cifValue != null ? req.cifValue : BigDecimal.ZERO);
    e.setPortOfEntry(req.portOfEntry);
    e.setBoeNumber(req.boeNumber);
    e.setBoeDate(req.boeDate);
    e.setBillOfLading(req.billOfLading);
    e.setImportDate(req.importDate);
    e.setStatus(req.status != null ? req.status : "Draft");
    e.setRemarks(req.remarks);

    // simple duty calculation using default rates
    ImportDutyTaxPreview preview = calculatePreview(e.getProductId(), e.getCifValue());
    e.setCdRate(preview.cdRate);
    e.setSdRate(preview.sdRate);
    e.setVatRate(preview.vatRate);
    e.setAitRate(preview.aitRate);
    e.setAtRate(preview.atRate);
    e.setCustomsDuty(preview.customsDuty);
    e.setSupplementaryDuty(preview.supplementaryDuty);
    e.setVat(preview.vat);
    e.setAdvanceIncomeTax(preview.advanceIncomeTax);
    e.setAdvanceTax(preview.advanceTax);
    e.setTotalPayable(preview.totalPayable);

    ImportDuty saved = importDutyDAO.save(e);
    return ImportDutyResponse.from(saved);
  }

  public List<ImportDutyResponse> listAll() {
    return importDutyDAO.findByIsDeletedFalse().stream()
        .map(ImportDutyResponse::from)
        .collect(Collectors.toList());
  }

  public ImportDutyResponse getById(Long id) {
    ImportDuty e = importDutyDAO.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Import duty record not found: " + id));
    return ImportDutyResponse.from(e);
  }

  public ImportDutyResponse update(Long id, ImportDutyUpdateRequest req) {
    ImportDuty e = importDutyDAO.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Import duty record not found: " + id));
    e.setTinNumber(req.tinNumber);
    e.setTaxpayerName(req.taxpayerName);
    e.setBusinessName(req.businessName);
    e.setProductId(req.productId);
    e.setGoodsDescription(req.goodsDescription);
    e.setOriginCountry(req.originCountry);
    e.setCifValue(req.cifValue != null ? req.cifValue : BigDecimal.ZERO);
    e.setPortOfEntry(req.portOfEntry);
    e.setBoeNumber(req.boeNumber);
    e.setBoeDate(req.boeDate);
    e.setBillOfLading(req.billOfLading);
    e.setImportDate(req.importDate);
    e.setStatus(req.status);
    e.setRemarks(req.remarks);

    ImportDutyTaxPreview preview = calculatePreview(e.getProductId(), e.getCifValue());
    e.setCdRate(preview.cdRate);
    e.setSdRate(preview.sdRate);
    e.setVatRate(preview.vatRate);
    e.setAitRate(preview.aitRate);
    e.setAtRate(preview.atRate);
    e.setCustomsDuty(preview.customsDuty);
    e.setSupplementaryDuty(preview.supplementaryDuty);
    e.setVat(preview.vat);
    e.setAdvanceIncomeTax(preview.advanceIncomeTax);
    e.setAdvanceTax(preview.advanceTax);
    e.setTotalPayable(preview.totalPayable);

    ImportDuty saved = importDutyDAO.save(e);
    return ImportDutyResponse.from(saved);
  }

  public ImportDutyTaxPreview preview(Long productId, BigDecimal cifValue) {
    return calculatePreview(productId, cifValue != null ? cifValue : BigDecimal.ZERO);
  }

  private ImportDutyTaxPreview calculatePreview(Long productId, BigDecimal cifValue) {
    // Basic fallback rates. For production these should come from product/master
    // data.
    BigDecimal cdRate = BigDecimal.valueOf(5); // 5%
    BigDecimal sdRate = BigDecimal.ZERO;
    BigDecimal vatRate = BigDecimal.valueOf(15);
    BigDecimal aitRate = BigDecimal.valueOf(5);
    BigDecimal atRate = BigDecimal.ZERO;

    BigDecimal customsDuty = cifValue.multiply(cdRate).divide(BigDecimal.valueOf(100));
    BigDecimal supplementaryDuty = cifValue.multiply(sdRate).divide(BigDecimal.valueOf(100));

    // VAT typically applied on CIF + CD + SD
    BigDecimal vatBase = cifValue.add(customsDuty).add(supplementaryDuty);
    BigDecimal vat = vatBase.multiply(vatRate).divide(BigDecimal.valueOf(100));

    BigDecimal advanceIncomeTax = cifValue.multiply(aitRate).divide(BigDecimal.valueOf(100));
    BigDecimal advanceTax = cifValue.multiply(atRate).divide(BigDecimal.valueOf(100));

    BigDecimal total = customsDuty.add(supplementaryDuty).add(vat).add(advanceIncomeTax).add(advanceTax);

    ImportDutyTaxPreview p = new ImportDutyTaxPreview();
    p.cifValue = cifValue;
    p.cdRate = cdRate;
    p.sdRate = sdRate;
    p.vatRate = vatRate;
    p.aitRate = aitRate;
    p.atRate = atRate;
    p.customsDuty = customsDuty;
    p.supplementaryDuty = supplementaryDuty;
    p.vat = vat;
    p.advanceIncomeTax = advanceIncomeTax;
    p.advanceTax = advanceTax;
    p.totalPayable = total;
    return p;
  }
}
