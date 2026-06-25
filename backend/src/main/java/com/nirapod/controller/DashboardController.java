package com.nirapod.controller;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nirapod.dao.DashboardDAO;
import com.nirapod.dao.FiscalYearDAO;
import com.nirapod.model.FiscalYear;
import com.nirapod.model.Payment;
import com.nirapod.model.Taxpayer;
import com.nirapod.model.VatReturn;
import com.nirapod.service.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

  @Autowired private TaxpayerService  taxpayerService;
  @Autowired private BusinessService  businessService;
  @Autowired private PaymentService   paymentService;
  @Autowired private AuditCaseService auditCaseService;
  @Autowired private VatReturnService vatReturnService;
  @Autowired private DashboardDAO     dashboardDAO;
  @Autowired private FiscalYearDAO    fiscalYearDAO;

  // ── /api/dashboard/stats?year=2025-26 ──────────────────────────────────
  @GetMapping("/stats")
  public ResponseEntity<Map<String, Object>> getStats(
      @RequestParam(required = false) String year) {

    // 1. Fiscal year date range for growth calculation
    FiscalYear fy = resolveFiscalYear(year);
    LocalDate curStart  = fy.getStartDate();
    LocalDate curEnd    = fy.getEndDate();
    long days           = java.time.temporal.ChronoUnit.DAYS.between(curStart, curEnd);
    LocalDate prevEnd   = curStart.minusDays(1);
    LocalDate prevStart = prevEnd.minusDays(days);

    int filterYear = parseYear(year);

    // 2. Audit KPIs — from audit_cases via updated getKpis()
    //    getKpis() now includes SUPERVISOR_REVIEW + RESPONSE_RECEIVED in openAudits
    Map<String, Object> auditKpis    = auditCaseService.getKpis();
    long pendingAudits   = toLong(auditKpis.get("openAudits"));
    long assessApproved  = toLong(auditKpis.get("assessmentApproved"));
    long demandIssued    = toLong(auditKpis.get("demandIssued"));
    long paid            = toLong(auditKpis.get("paid"));
    long appealed        = toLong(auditKpis.get("appealed"));
    long closed          = toLong(auditKpis.get("closed"));
    long completedAudits = demandIssued + assessApproved + paid + closed;
    long totalAudits     = pendingAudits + completedAudits + appealed;

    // 3. Taxpayers — DB-level count, respects isDeleted
    long totalTaxpayers     = dashboardDAO.countTotalTaxpayers();
    long taxpayersThisMonth = dashboardDAO.countTaxpayersThisMonth();

    // 4. Businesses
    long totalBusinesses = businessService.getAll().size();

    // 5. Payments filtered by fiscal year (stream is small — only payments table, 3 rows in dev)
    List<Payment> allPayments = paymentService.getAll();
    List<Payment> payments = allPayments.stream()
        .filter(p -> p.getPaymentDate() == null
            || p.getPaymentDate().getYear() == filterYear
            || p.getPaymentDate().getYear() == filterYear + 1)
        .collect(Collectors.toList());

    double totalRevenue = payments.stream()
            .filter(p -> !"Failed".equals(p.getStatus()) && !"Cancelled".equals(p.getStatus()))
            .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0)
            .sum();

    long todayEntries = payments.stream()
        .filter(p -> p.getPaymentDate() != null && p.getPaymentDate().equals(LocalDate.now()))
        .count();

    // 6. VAT returns filtered by fiscal year
    List<VatReturn> vatReturns = vatReturnService.getAllReturns().stream()
        .filter(r -> r.getPeriodYear() == null
            || r.getPeriodYear().contains(String.valueOf(filterYear))
            || r.getPeriodYear().contains(String.valueOf(filterYear + 1)))
        .collect(Collectors.toList());

    long totalVatReturns  = vatReturns.size();
    long submittedReturns = vatReturns.stream()
        .filter(r -> !"Draft".equals(r.getStatus()))
        .count();

    // 7. Refunds — DB-level count with CORRECT status values
    //    FIX: old code used "Pending".equals() — matched nothing.
    //    Real statuses: SUBMITTED, UNDER_VERIFICATION, INFO_REQUESTED, RESPONSE_RECEIVED
    long pendingRefunds = dashboardDAO.countPendingRefunds();

    // 8. Penalties — DB-level count
    long issuedPenalties = dashboardDAO.countIssuedPenalties();

    // 9. Growth — real DB calculations, NOT hardcoded 5.2 / 8.7 / -5.2
    double taxpayerGrowth  = dashboardDAO.computeTaxpayerGrowth (curStart, curEnd, prevStart, prevEnd);
    double revenueGrowth   = dashboardDAO.computeRevenueGrowth  (curStart, curEnd, prevStart, prevEnd);
    double vatReturnGrowth = dashboardDAO.computeVatReturnGrowth(curStart, curEnd, prevStart, prevEnd);
    double paymentGrowth   = dashboardDAO.computePaymentGrowth  (curStart, curEnd, prevStart, prevEnd);
    double auditGrowth     = dashboardDAO.computeAuditGrowth    (curStart, curEnd, prevStart, prevEnd);
    double refundGrowth    = dashboardDAO.computeRefundGrowth   (curStart, curEnd, prevStart, prevEnd);

    // 10. Build response
    Map<String, Object> stats = new LinkedHashMap<>();
    stats.put("totalTaxpayers",          totalTaxpayers);
    stats.put("totalBusinesses",         totalBusinesses);
    stats.put("totalPayments",           payments.size());
    stats.put("totalRevenue",            totalRevenue);
    stats.put("totalVatReturns",         totalVatReturns);
    stats.put("submittedVatReturns",     submittedReturns);
    stats.put("pendingAudits",           pendingAudits);
    stats.put("pendingRefunds",          pendingRefunds);
    stats.put("issuedPenalties",         issuedPenalties);
    stats.put("taxpayerGrowth",          taxpayerGrowth);
    stats.put("revenueGrowth",           revenueGrowth);
    stats.put("vatReturnGrowth",         vatReturnGrowth);
    stats.put("paymentGrowth",           paymentGrowth);
    stats.put("totalAudits",             totalAudits);
    stats.put("completedAudits",         completedAudits);
    stats.put("flaggedCases",            0L);
    stats.put("auditGrowth",             auditGrowth);
    stats.put("refundGrowth",            refundGrowth);
    stats.put("todayEntries",            todayEntries);
    stats.put("pendingTasks",            pendingAudits + pendingRefunds);
    stats.put("taxpayersAddedThisMonth", taxpayersThisMonth);
    stats.put("myVatReturns",            0);
    stats.put("myPayments",              0);
    stats.put("myPendingNotices",        0);
    stats.put("myRefundStatus",          0);
    stats.put("myTotalPaid",             0.0);
    return ResponseEntity.ok(stats);
  }

  // ── /api/dashboard/recent-taxpayers ──────────────────────────────────────
  @GetMapping("/recent-taxpayers")
  public ResponseEntity<List<Map<String, Object>>> getRecentTaxpayers() {
    List<Taxpayer> all = taxpayerService.getAll();
    List<Map<String, Object>> result = all.stream()
        .filter(t -> !t.isDeleted())   
        .sorted(Comparator.comparingLong(Taxpayer::getId).reversed())
        .limit(5)
        .map(t -> {
          Map<String, Object> m = new LinkedHashMap<>();
          m.put("id",               t.getId());
          m.put("tin",              t.getTinNumber());
          m.put("fullName",         t.getFullName() != null ? t.getFullName() : t.getCompanyName());
          m.put("email",            t.getEmail());
          m.put("phone",            t.getPhone());
          m.put("status",           t.getStatus());
          m.put("registrationDate", t.getRegistrationDate());
          return m;
        })
        .collect(Collectors.toList());
    return ResponseEntity.ok(result);
  }

  // ── /api/dashboard/recent-payments ───────────────────────────────────────
  @GetMapping("/recent-payments")
  public ResponseEntity<List<Map<String, Object>>> getRecentPayments() {
    List<Payment> all = paymentService.getAll();
    List<Map<String, Object>> result = all.stream()
        .sorted(Comparator.comparingLong(Payment::getId).reversed())
        .limit(5)
        .map(p -> {
          String name = p.getTaxpayerName();
          if ((name == null || name.isBlank()) && p.getTaxpayer() != null) {
            Taxpayer t = p.getTaxpayer();
            name = t.getFullName() != null ? t.getFullName() : t.getCompanyName();
          }
          Map<String, Object> m = new LinkedHashMap<>();
          m.put("id",            p.getId());
          m.put("transactionId", p.getTransactionId());
          m.put("taxpayerName",  name != null ? name : "N/A");
          m.put("amount",        p.getAmount());
          m.put("paymentType",   p.getPaymentType());
          m.put("paymentDate",   p.getPaymentDate());
          m.put("status",        p.getStatus());
          return m;
        })
        .collect(Collectors.toList());
    return ResponseEntity.ok(result);
  }

  // ── /api/dashboard/vat-chart?year=2025-26 ────────────────────────────────
  @GetMapping("/vat-chart")
  public ResponseEntity<List<Map<String, Object>>> getVatChart(
      @RequestParam(required = false) String year) {
    int filterYear = parseYear(year);
    Map<String, String> fullToAbbr = buildMonthMap();

    Map<String, Double> totals = vatReturnService.getAllReturns().stream()
        .filter(r -> r.getPeriodYear() != null
            && (r.getPeriodYear().contains(String.valueOf(filterYear))
                || r.getPeriodYear().contains(String.valueOf(filterYear + 1)))
            && !"Draft".equals(r.getStatus()))
        .collect(Collectors.groupingBy(
            r -> {
              String pm = r.getPeriodMonth() == null ? "" : r.getPeriodMonth().trim();
              return fullToAbbr.getOrDefault(pm.toLowerCase(), pm);
            },
            Collectors.summingDouble(r -> r.getOutputTax() != null ? r.getOutputTax() : 0.0)
        ));

    return ResponseEntity.ok(buildMonthChart(totals));
  }

  // ── /api/dashboard/payment-chart?year=2025-26 ────────────────────────────
  @GetMapping("/payment-chart")
  public ResponseEntity<List<Map<String, Object>>> getPaymentChart(
      @RequestParam(required = false) String year) {
    int filterYear = parseYear(year);

    Map<String, Double> totals = paymentService.getAll().stream()
    		.filter(p -> p.getPaymentDate() != null
            && (p.getPaymentDate().getYear() == filterYear
                || p.getPaymentDate().getYear() == filterYear + 1)
            && !"Failed".equals(p.getStatus())
            && !"Cancelled".equals(p.getStatus()))
        .collect(Collectors.groupingBy(
            p -> p.getPaymentDate().getMonth()
                  .getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
            Collectors.summingDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
        ));

    return ResponseEntity.ok(buildMonthChart(totals));
  }

  // ── /api/dashboard/zone-vat?year=2025-26 ─────────────────────────────────
  // Returns [] when no data — frontend must NOT fall back to mock data
  @GetMapping("/zone-vat")
  public ResponseEntity<List<Map<String, Object>>> getZoneVat(
      @RequestParam(required = false) String year) {
    int filterYear = parseYear(year);
    String[] colors = { "#1faa8b", "#1a3f8f", "#7c3aed", "#f59e0b", "#0891b2" };

    Map<String, Double> byZone = new LinkedHashMap<>();
    for (VatReturn r : vatReturnService.getAllReturnsWithZone()) {
      if ("Draft".equals(r.getStatus())) continue;
      if (r.getPeriodYear() == null) continue;
      if (!r.getPeriodYear().contains(String.valueOf(filterYear))
          && !r.getPeriodYear().contains(String.valueOf(filterYear + 1))) continue;

      String zoneName = "Unassigned";
      if (r.getTaxpayer() != null && r.getTaxpayer().getTaxZone() != null) {
        String zn = r.getTaxpayer().getTaxZone().getName();
        if (zn != null && !zn.isBlank()) zoneName = zn;
      }
      double crore = (r.getOutputTax() != null ? r.getOutputTax() : 0.0) / 10_000_000.0;
      byZone.merge(zoneName, crore, Double::sum);
    }

    byZone.remove("Unassigned");
    if (byZone.isEmpty()) return ResponseEntity.ok(List.of());

    List<Map<String, Object>> result = new ArrayList<>();
    int ci = 0;
    for (Map.Entry<String, Double> entry : byZone.entrySet()) {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("name",       entry.getKey());
      m.put("collection", Math.round(entry.getValue() * 10.0) / 10.0);
      m.put("target",     0);
      m.put("color",      colors[ci % colors.length]);
      result.add(m);
      ci++;
    }
    return ResponseEntity.ok(result);
  }

  // ── Private helpers ───────────────────────────────────────────────────────

  private FiscalYear resolveFiscalYear(String yearParam) {
    if (yearParam != null && !yearParam.isBlank()) {
      for (FiscalYear fy : fiscalYearDAO.findAllByOrderByYearNameDesc()) {
        if (yearParam.equals(fy.getYearName())) return fy;
      }
    }
    List<FiscalYear> current = fiscalYearDAO.findCurrentYears();
    if (!current.isEmpty()) return current.get(0);

    // absolute fallback
    FiscalYear fb = new FiscalYear();
    int y = LocalDate.now().getYear();
    fb.setStartDate(LocalDate.of(y, 7, 1));
    fb.setEndDate(LocalDate.of(y + 1, 6, 30));
    return fb;
  }

  private int parseYear(String year) {
    return (year != null && year.contains("-"))
        ? Integer.parseInt(year.split("-")[0])
        : LocalDate.now().getYear();
  }

  private Map<String, String> buildMonthMap() {
    Map<String, String> map = new LinkedHashMap<>();
    for (Month m : Month.values()) {
      map.put(m.getDisplayName(TextStyle.FULL,  Locale.ENGLISH).toLowerCase(),
              m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
      map.put(m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toLowerCase(),
              m.getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
    }
    return map;
  }

  private List<Map<String, Object>> buildMonthChart(Map<String, Double> totals) {
    return List.of("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
        .stream()
        .map(abbr -> {
          Map<String, Object> p = new LinkedHashMap<>();
          p.put("label", abbr);
          p.put("value", totals.getOrDefault(abbr, 0.0));
          return p;
        })
        .collect(Collectors.toList());
  }

  private long toLong(Object val) {
    if (val instanceof Number) return ((Number) val).longValue();
    return 0L;
  }
}