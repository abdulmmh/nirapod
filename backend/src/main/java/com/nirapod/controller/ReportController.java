package com.nirapod.controller;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dto.request.ReportExportRequest;
import com.nirapod.dto.response.*;
import com.nirapod.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.nirapod.dto.response.AitReportRowDTO;
import com.nirapod.dto.response.ImportDutyReportRowDTO;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

  private final ReportService reportService;

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }


  @GetMapping("/kpi-summary")
  public ResponseEntity<KpiSummaryDTO> getKpiSummary(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "") String zone,
      @RequestParam(defaultValue = "") String circle,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(reportService.getKpiSummary(fiscalYear, zone, circle, principal));
  }


  @GetMapping("/revenue-trend")
  public ResponseEntity<List<TrendDataDTO>> getRevenueTrend(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "12") int months,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(reportService.getRevenueTrend(fiscalYear, months, principal));
  }


  @GetMapping("/zone-performance")
  public ResponseEntity<List<ZonePerformanceDTO>> getZonePerformance(
      @RequestParam String fiscalYear,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(reportService.getZonePerformance(fiscalYear, principal));
  }


  @GetMapping("/compliance-rate")
  public ResponseEntity<List<ComplianceReportDTO>> getComplianceRate(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "") String zone,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(reportService.getComplianceRate(fiscalYear, zone, principal));
  }

  @GetMapping("/vat-collection")
  public ResponseEntity<Page<VatCollectionRowDTO>> getVatCollection(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "") String zone,
      @RequestParam(defaultValue = "") String circle,
      @RequestParam(defaultValue = "") String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(
        reportService.getVatCollection(fiscalYear, zone, circle, status,
            PageRequest.of(page, size), principal));
  }


  @GetMapping("/income-tax")
  public ResponseEntity<Page<IncomeTaxRowDTO>> getIncomeTax(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "") String status,
      @RequestParam(defaultValue = "") String taxpayerType,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(
        reportService.getIncomeTax(fiscalYear, status, taxpayerType,
            PageRequest.of(page, size), principal));
  }


  @GetMapping("/penalty-collection")
  public ResponseEntity<Page<PenaltyRowDTO>> getPenaltyCollection(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "") String severity,
      @RequestParam(defaultValue = "") String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(
        reportService.getPenaltyCollection(fiscalYear, severity, status,
            PageRequest.of(page, size), principal));
  }


  @GetMapping("/refund-status")
  public ResponseEntity<Page<RefundRowDTO>> getRefundStatus(
      @RequestParam String fiscalYear,
      @RequestParam(defaultValue = "") String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @AuthenticationPrincipal AuthPrincipal principal) {
    return ResponseEntity.ok(
        reportService.getRefundStatus(fiscalYear, status,
            PageRequest.of(page, size), principal));
  }


  @PostMapping("/export/pdf")
  public void exportPdf(
      @RequestBody ReportExportRequest req,
      @AuthenticationPrincipal AuthPrincipal principal,
      HttpServletResponse response) throws IOException {
    byte[] pdf = reportService.exportAsPdf(req, principal);
    String filename = req.getReportType() + "_" + req.getFiscalYear() + ".pdf";
    response.setContentType("application/pdf");
    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
    response.setContentLength(pdf.length);
    response.getOutputStream().write(pdf);
  }

  @PostMapping("/export/excel")
  public ResponseEntity<byte[]> exportExcel(
      @RequestBody ReportExportRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    byte[] xlsx = reportService.exportAsExcel(req, principal);
    String filename = req.getReportType() + "_" + req.getFiscalYear() + ".xlsx";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .contentType(MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(xlsx);
  }

  @PostMapping("/export/csv")
  public ResponseEntity<byte[]> exportCsv(
      @RequestBody ReportExportRequest req,
      @AuthenticationPrincipal AuthPrincipal principal) {
    byte[] csv = reportService.exportAsCsv(req, principal);
    String filename = req.getReportType() + "_" + req.getFiscalYear() + ".csv";
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(csv);
  }
  
  @GetMapping("/ait-deduction")
  public ResponseEntity<Page<AitReportRowDTO>> getAitDeductionReport(
          @RequestParam String fiscalYear,
          @RequestParam(defaultValue = "") String status,
          @RequestParam(defaultValue = "") String sourceType,
          @RequestParam(defaultValue = "0")  int page,
          @RequestParam(defaultValue = "20") int size,
          @AuthenticationPrincipal AuthPrincipal principal) {
      return ResponseEntity.ok(
              reportService.getAitDeductionReport(
                      fiscalYear, status, sourceType,
                      PageRequest.of(page, size), principal));
  }
  
  @GetMapping("/import-duty")
  public ResponseEntity<Page<ImportDutyReportRowDTO>> getImportDutyReport(
          @RequestParam String fiscalYear,
          @RequestParam(defaultValue = "") String status,
          @RequestParam(defaultValue = "0")  int page,
          @RequestParam(defaultValue = "20") int size,
          @AuthenticationPrincipal AuthPrincipal principal) {
      return ResponseEntity.ok(
              reportService.getImportDutyReport(
                      fiscalYear, status,
                      PageRequest.of(page, size), principal));
  }
}
