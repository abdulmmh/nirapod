package com.nirapod.service;

import com.nirapod.config.AuthPrincipal;
import com.nirapod.dao.*;
import com.nirapod.dto.request.ReportExportRequest;
import com.nirapod.dto.response.*;
import com.nirapod.model.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class ReportService {

    private static final String ROLE_TAXPAYER = "TAXPAYER";

    private final VatReturnDAO       vatReturnDAO;
    private final IncomeTaxReturnDAO incomeTaxReturnDAO;
    private final PenaltyDAO         penaltyDAO;
    private final RefundDAO          refundDAO;
    private final AitRecordDAO       aitRecordDAO;
    private final TaxpayerDAO        taxpayerDAO;
    private final TaxZoneDAO         taxZoneDAO;
    private final ImportDutyDAO  importDutyDAO;
    private final ReportExportService exportService;

    public ReportService(VatReturnDAO vatReturnDAO,
                         IncomeTaxReturnDAO incomeTaxReturnDAO,
                         PenaltyDAO penaltyDAO,
                         RefundDAO refundDAO,
                         AitRecordDAO aitRecordDAO,
                         TaxpayerDAO taxpayerDAO,
                         TaxZoneDAO taxZoneDAO,
                         ImportDutyDAO importDutyDAO,
                         ReportExportService exportService
    		) {
        this.vatReturnDAO       = vatReturnDAO;
        this.incomeTaxReturnDAO = incomeTaxReturnDAO;
        this.penaltyDAO         = penaltyDAO;
        this.refundDAO          = refundDAO;
        this.aitRecordDAO       = aitRecordDAO;
        this.taxpayerDAO        = taxpayerDAO;
        this.taxZoneDAO         = taxZoneDAO;
        this.importDutyDAO 		= importDutyDAO;
        this.exportService 		= exportService;
    }


    @Cacheable(value = "kpiSummary", key = "#fiscalYear + '_' + #zone + '_' + #circle")
    public KpiSummaryDTO getKpiSummary(String fiscalYear, String zone,
                                        String circle, AuthPrincipal principal) {
        KpiSummaryDTO dto = new KpiSummaryDTO();
        dto.setFiscalYear(fiscalYear);
        dto.setGeneratedAt(LocalDateTime.now());


        Long userId = isTaxpayer(principal) ? principal.getUserId() : null;

        Long zoneId = parseZoneId(zone);                   
        BigDecimal vatTotal = safeVal(vatReturnDAO.sumNetTaxPayableByAssessmentYear(
               fiscalYear, zoneId, circle, userId));
        BigDecimal vatPrevTotal = safeVal(vatReturnDAO.sumNetTaxPayableByAssessmentYear(
               prevFiscalYear(fiscalYear), zoneId, circle, userId));
        dto.setVatCollected(vatTotal);
        dto.setVatGrowthPct(calcGrowthPct(vatPrevTotal, vatTotal));

        BigDecimal itTotal    = safeVal(incomeTaxReturnDAO.sumGrossTaxByAssessmentYear(
                                    fiscalYear, zone, circle, userId));
        BigDecimal itPrevTotal = safeVal(incomeTaxReturnDAO.sumGrossTaxByAssessmentYear(
                                    prevFiscalYear(fiscalYear), zone, circle, userId));
        dto.setIncomeTaxCollected(itTotal);
        dto.setItGrowthPct(calcGrowthPct(itPrevTotal, itTotal));

        BigDecimal importTotal = safeVal(
                aitRecordDAO.sumImportDutyByFiscalYear(fiscalYear, zone, userId));
        BigDecimal importPrev  = safeVal(
                aitRecordDAO.sumImportDutyByFiscalYear(prevFiscalYear(fiscalYear), zone, userId));
        dto.setImportDutyCollected(importTotal);
        dto.setImportDutyGrowthPct(calcGrowthPct(importPrev, importTotal));

        BigDecimal aitTotal = safeVal(
                aitRecordDAO.sumAitDeductedByFiscalYear(fiscalYear, zone, userId));
        dto.setAitDeducted(aitTotal);

        BigDecimal penaltyTotal = safeVal(
                penaltyDAO.sumTotalAmountByAssessmentYear(fiscalYear, userId));
        dto.setPenaltyCollected(penaltyTotal);

        BigDecimal refundTotal = safeVal(
                refundDAO.sumApprovedRefundByFiscalYearName(fiscalYear, userId));
        dto.setRefundPaid(refundTotal);
        dto.setTotalRevenue(vatTotal.add(itTotal).add(importTotal));

        long activeTaxpayers = taxpayerDAO.countActiveApproved();
        dto.setActiveTaxpayers((int) activeTaxpayers);

        long filedReturns = incomeTaxReturnDAO.countByAssessmentYearAndStatus(
                fiscalYear, "Submitted");
        dto.setFiledReturns((int) filedReturns);

        double complianceRate = activeTaxpayers > 0
                ? Math.min(filedReturns * 100.0 / activeTaxpayers, 100.0) : 0.0;
        dto.setComplianceRate(complianceRate);

        return dto;
    }



    @Cacheable(value = "revenueTrend", key = "#fiscalYear + '_' + #months")
    public List<TrendDataDTO> getRevenueTrend(String fiscalYear, int months,
                                               AuthPrincipal principal) {
        Long userId = isTaxpayer(principal) ? principal.getUserId() : null;

        List<Object[]> rows = vatReturnDAO.monthlyTrendByAssessmentYear(
               fiscalYear, null, userId, PageRequest.of(0, months)); 

        return rows.stream().map(row -> {
            TrendDataDTO t = new TrendDataDTO();
            t.setLabel(row[0] != null ? row[0].toString() : "");
            t.setVatAmount(toBigDecimal(row[1]));
            t.setItAmount(BigDecimal.ZERO);
            t.setImportDutyAmount(BigDecimal.ZERO);
            t.setTotalAmount(t.getVatAmount());
            return t;
        }).collect(Collectors.toList());
    }


    @Cacheable(value = "zonePerformance", key = "#fiscalYear")
    public List<ZonePerformanceDTO> getZonePerformance(String fiscalYear,
                                                        AuthPrincipal principal) {
        List<TaxZone> zones = taxZoneDAO.findAll();
        List<ZonePerformanceDTO> result = new ArrayList<>();

        for (TaxZone zone : zones) {
            ZonePerformanceDTO dto = new ZonePerformanceDTO();
            dto.setZoneId(zone.getId());
            dto.setZoneName(zone.getName());

            BigDecimal vatAmt = safeVal(vatReturnDAO.sumNetTaxPayableByAssessmentYear(
            	           fiscalYear, zone.getId(), "", null));
            
            BigDecimal itAmt  = safeVal(incomeTaxReturnDAO.sumGrossTaxByAssessmentYear(
                    fiscalYear, zone.getName(), "", null));
            dto.setTotalCollection(vatAmt.add(itAmt));

            dto.setTaxpayerCount((int) taxpayerDAO.countByZone(zone.getId()));

            dto.setFiledCount( incomeTaxReturnDAO.countByAssessmentYearAndZone(
                    fiscalYear, zone.getId()));

            result.add(dto);
        }
        return result;
    }


    @Cacheable(value = "complianceRate", key = "#fiscalYear + '_' + #zone")
    public List<ComplianceReportDTO> getComplianceRate(String fiscalYear, String zone,
                                                        AuthPrincipal principal) {
       List<TaxZone> zones = (zone != null && !zone.isBlank())
                ? taxZoneDAO.findAll().stream()
                    .filter(z -> z.getId() != null
                              && z.getName().toLowerCase().contains(zone.toLowerCase()))
                    .collect(Collectors.toList())
                : taxZoneDAO.findAll();

        return zones.stream().map(z -> {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setZoneName(z.getName());
            int total = (int) taxpayerDAO.countByZone(z.getId());
            int filed = (int) incomeTaxReturnDAO.countByAssessmentYearAndZone(
                    fiscalYear, z.getId());
            dto.setTotalRegistered(total);
            dto.setFiledCount(filed);
            dto.setComplianceRate(
                    total > 0 ? Math.min(filed * 100.0 / total, 100.0) : 0.0);
            return dto;
        }).collect(Collectors.toList());
    }
    
    
    public Page<AitReportRowDTO> getAitDeductionReport(
            String fiscalYear, String status, String sourceType,
            Pageable pageable, AuthPrincipal principal) {

        Long taxpayerId = isTaxpayer(principal) ? principal.getUserId() : null;

        return aitRecordDAO
                .findByFiscalYearAndFilters(fiscalYear, status, sourceType,
                                            taxpayerId, pageable)
                .map(AitReportRowDTO::from);
    }
    
    public Page<ImportDutyReportRowDTO> getImportDutyReport(
            String fiscalYear, String status,
            Pageable pageable, AuthPrincipal principal) {

        int[] years = parseFiscalYearRange(fiscalYear);

        return importDutyDAO
                .findByYearRangeAndStatus(years[0], years[1],
                        status != null ? status : "", pageable)
                .map(ImportDutyReportRowDTO::from);
    }

 
    public Page<VatCollectionRowDTO> getVatCollection(String fiscalYear, String zone,
            String circle, String status, Pageable pageable, AuthPrincipal principal) {
        Long userId  = isTaxpayer(principal) ? principal.getUserId() : null;
        Long zoneId  = parseZoneId(zone);     // ← parse String → Long
        return vatReturnDAO
                .findByAssessmentYearAndFilters(
                        fiscalYear, zoneId, circle, status, userId, pageable)
                .map(VatCollectionRowDTO::from);
    }

  
    public Page<IncomeTaxRowDTO> getIncomeTax(String fiscalYear, String status,
            String taxpayerType, Pageable pageable, AuthPrincipal principal) {
        Long userId = isTaxpayer(principal) ? principal.getUserId() : null;
        return incomeTaxReturnDAO
                .findByAssessmentYearAndFilters(fiscalYear, status, taxpayerType, userId, pageable)
                .map(IncomeTaxRowDTO::from);
    }

     public Page<PenaltyRowDTO> getPenaltyCollection(String fiscalYear, String severity,
            String status, Pageable pageable, AuthPrincipal principal) {
        Long userId = isTaxpayer(principal) ? principal.getUserId() : null;
        return penaltyDAO
                .findByAssessmentYearAndFilters(fiscalYear, severity, status, userId, pageable)
                .map(PenaltyRowDTO::from);
    }


    public Page<RefundRowDTO> getRefundStatus(String fiscalYear, String status,
            Pageable pageable, AuthPrincipal principal) {
        Long userId = isTaxpayer(principal) ? principal.getUserId() : null;
        return refundDAO
                .findByFiscalYearNameAndFilters(fiscalYear, status, userId, pageable)
                .map(RefundRowDTO::from);
    }

  
    public byte[] exportAsPdf(ReportExportRequest req, AuthPrincipal principal) {
        return exportService.exportAsPdf(req, principal);
    }

    public byte[] exportAsExcel(ReportExportRequest req, AuthPrincipal principal) {
        return exportService.exportAsExcel(req, principal);
    }

    public byte[] exportAsCsv(ReportExportRequest req, AuthPrincipal principal) {
        StringBuilder sb = new StringBuilder();
        switch (req.getReportType()) {
            case "VAT_COLLECTION":
                sb.append("BIN No,Business Name,Zone,Circle,Period,Gross Supplies,Output Tax,Input Tax,Net VAT,Status,Submitted At\n");
                getVatCollection(req.getFiscalYear(),
                        req.getZone() != null ? req.getZone() : "",
                        req.getCircle() != null ? req.getCircle() : "",
                        "", Pageable.unpaged(), principal)
                    .forEach(r -> sb.append(String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%s,%s%n",
                        r.getBinNo(), escapeCsv(r.getBusinessName()),
                        nvl(r.getZone()), nvl(r.getCircle()), nvl(r.getPeriod()),
                        dec(r.getGrossSales()), dec(r.getOutputVat()),
                        dec(r.getInputVatCredit()), dec(r.getNetVat()),
                        r.getStatus(), nvl(r.getSubmittedAt()))));
                break;
 
            case "INCOME_TAX":
                sb.append("TIN,Taxpayer Name,Category,Assessment Year,Gross Income,Tax Payable,Tax Paid,Tax Due,Status,Filed At\n");
                getIncomeTax(req.getFiscalYear(), nvl(req.getStatus()), "",
                        Pageable.unpaged(), principal)
                    .forEach(r -> sb.append(String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%s,%s%n",
                        r.getTinNumber(), escapeCsv(r.getTaxpayerName()),
                        nvl(r.getTaxpayerType()), nvl(r.getAssessmentYear()),
                        dec(r.getGrossIncome()), dec(r.getTaxPayable()),
                        dec(r.getTaxPaid()), dec(r.getTaxDue()),
                        r.getStatus(), nvl(r.getFiledAt()))));
                break;
 
            case "PENALTY_COLLECTION":
                sb.append("Penalty No,Taxpayer,TIN,Type,Severity,Base Amount,Interest,Total,Status,Issued At\n");
                getPenaltyCollection(req.getFiscalYear(), "", "",
                        Pageable.unpaged(), principal)
                    .forEach(r -> sb.append(String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%s,%s%n",
                        r.getPenaltyRefNo(), escapeCsv(r.getTaxpayerName()),
                        nvl(r.getTinNumber()), nvl(r.getPenaltyType()),
                        nvl(r.getSeverity()),
                        dec(r.getBaseAmount()), dec(r.getInterestAmount()),
                        dec(r.getTotalAmount()), r.getStatus(),
                        nvl(r.getIssuedAt()))));
                break;
 
            case "REFUND_STATUS":
                sb.append("Reference No,Taxpayer,TIN,Type,Claimed,Approved,Paid,Status,Submitted At\n");
                getRefundStatus(req.getFiscalYear(), "", Pageable.unpaged(), principal)
                    .forEach(r -> sb.append(String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f,%s,%s%n",
                        r.getRefundRefNo(), escapeCsv(r.getTaxpayerName()),
                        nvl(r.getTinNumber()), nvl(r.getRefundType()),
                        dec(r.getClaimedAmount()), dec(r.getApprovedAmount()),
                        dec(r.getPaidAmount()), r.getStatus(),
                        nvl(r.getSubmittedAt()))));
                break;
 
            default:
                sb.append("Report type ").append(req.getReportType())
                  .append(" not supported for CSV export\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

 
    private boolean isTaxpayer(AuthPrincipal principal) {
        return principal != null && ROLE_TAXPAYER.equalsIgnoreCase(principal.getRole());
    }

    private BigDecimal safeVal(BigDecimal val) {
        return val != null ? val : BigDecimal.ZERO;
    }

    private BigDecimal toBigDecimal(Object obj) {
        if (obj == null) return BigDecimal.ZERO;
        if (obj instanceof BigDecimal) return (BigDecimal) obj;
        return new BigDecimal(obj.toString());
    }

    private double calcGrowthPct(BigDecimal prev, BigDecimal current) {
        if (prev == null || prev.compareTo(BigDecimal.ZERO) == 0) return 0.0;
        return current.subtract(prev)
                      .divide(prev, 4, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .doubleValue();
    }

    private String prevFiscalYear(String fy) {
    	
        if (fy == null || fy.isBlank()) return "";
        if (fy.contains("-")) {
            // Format: "2024-25"
            String[] parts = fy.split("-");
            try {
                int startYear = Integer.parseInt(parts[0]);
                int endYY     = Integer.parseInt(parts[1]);
                return (startYear - 1) + "-" + String.format("%02d", endYY - 1);
            } catch (NumberFormatException e) {
                return "";
            }
        }
        // Format: "2024"
        try {
            return String.valueOf(Integer.parseInt(fy) - 1);
        } catch (NumberFormatException e) {
            return "";
        }
    }
    
    private String nvl(String s) {
        return s != null ? s : "";
    }
 
    private double dec(java.math.BigDecimal v) {
        return v != null ? v.doubleValue() : 0.0;
    }
 
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    private Long parseZoneId(String zone) {
        if (zone == null || zone.isBlank()) return null;
        try {
            return Long.parseLong(zone);
        } catch (NumberFormatException e) {
            return null; 
        }
    }
    
    private int[] parseFiscalYearRange(String fiscalYear) {
        if (fiscalYear == null || fiscalYear.isBlank()) {
            int currentYear = java.time.LocalDate.now().getYear();
            return new int[]{currentYear, currentYear};
        }
        if (fiscalYear.contains("-")) {
            String[] parts = fiscalYear.split("-");
            try {
                int startYear = Integer.parseInt(parts[0]);
                int endYear = parts[1].length() == 2
                        ? startYear - (startYear % 100) + Integer.parseInt(parts[1])
                        : Integer.parseInt(parts[1]);
                return new int[]{startYear, endYear};
            } catch (NumberFormatException e) {
                int currentYear = java.time.LocalDate.now().getYear();
                return new int[]{currentYear, currentYear};
            }
        }
        try {
            int year = Integer.parseInt(fiscalYear);
            return new int[]{year, year};
        } catch (NumberFormatException e) {
            int currentYear = java.time.LocalDate.now().getYear();
            return new int[]{currentYear, currentYear};
        }
    }
}