package com.nirapod.service;

import com.nirapod.dao.AitRecordDAO;
import com.nirapod.dao.IncomeTaxReturnDAO;
import com.nirapod.dao.RefundDAO;
import com.nirapod.dto.response.RefundSourceResponse;
import com.nirapod.model.AitRecord;
import com.nirapod.model.IncomeTaxReturn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RefundSourceService {

    @Autowired private IncomeTaxReturnDAO itrDAO;
    @Autowired private AitRecordDAO       aitRecordDAO;
    @Autowired private RefundDAO          refundDAO;

      // ── ITR Sources ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<RefundSourceResponse> getEligibleItrSources(Long taxpayerId) {
        return itrDAO.findEligibleForRefund(taxpayerId).stream()
            .filter(itr -> !refundDAO.existsActiveRefundForSourceRecord(
                taxpayerId, "ITR", itr.getId()))
            .map(this::mapItrToSourceResponse)
            .filter(r -> r.getExcessAmount() != null &&
                         r.getExcessAmount().compareTo(java.math.BigDecimal.ZERO) > 0)
            .collect(Collectors.toList());
    }

    // ── AIT Sources ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<RefundSourceResponse> getEligibleAitSources(Long taxpayerId) {
        return aitRecordDAO.getByTaxpayerId(taxpayerId).stream()
            .filter(a -> "APPROVED".equals(a.getStatus()) || "SUBMITTED".equals(a.getStatus()))
            .filter(a -> computeAitExcess(a).compareTo(BigDecimal.ZERO) > 0)
            .filter(a -> !refundDAO.existsActiveRefundForSourceRecord(
                taxpayerId, "AIT", a.getId()))
            .map(this::mapAitToSourceResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RefundSourceResponse> getEligibleVatSources(Long taxpayerId) {
        return List.of();
    }

    @Transactional(readOnly = true)
    public List<RefundSourceResponse> getEligiblePaymentSources(Long taxpayerId) {
        return List.of();
    }

    // ── Calculate ─────────────────────────────────────────────────────────────

 
    @Transactional(readOnly = true)
    public Map<String, Object> calculateEligibleAmount(
            Long taxpayerId, String sourceType, List<Long> sourceRecordIds) {

        if (sourceRecordIds == null || sourceRecordIds.isEmpty())
            throw new IllegalArgumentException(
                "At least one source record must be selected.");

        BigDecimal totalTaxPaid   = BigDecimal.ZERO;
        BigDecimal totalLiability = BigDecimal.ZERO;
        BigDecimal totalExcess    = BigDecimal.ZERO;

      
        if ("ITR".equalsIgnoreCase(sourceType) || "INCOME_TAX".equalsIgnoreCase(sourceType)) {


            List<IncomeTaxReturn> records =
                itrDAO.findByIdsAndTaxpayerId(sourceRecordIds, taxpayerId);

            if (records.isEmpty()) {
                throw new IllegalArgumentException(
                    "No ITR records found for the selected IDs and taxpayer. " +
                    "Please go back and reselect the source records.");
            }

            for (IncomeTaxReturn itr : records) {
                BigDecimal paid      = computeTotalPaid(itr);
                BigDecimal liability = computeNetPayable(itr);
                BigDecimal excess    = paid.subtract(liability).max(BigDecimal.ZERO);
                totalTaxPaid   = totalTaxPaid.add(paid);
                totalLiability = totalLiability.add(liability);
                totalExcess    = totalExcess.add(excess);
            }

        } else if ("AIT".equalsIgnoreCase(sourceType) || "AIT_RECORD".equalsIgnoreCase(sourceType)) {
            for (Long id : sourceRecordIds) {
                Optional<AitRecord> opt = aitRecordDAO.getById(id);
                if (opt.isEmpty()) continue;
                AitRecord a = opt.get();
                BigDecimal excess = computeAitExcess(a);
                totalTaxPaid = totalTaxPaid.add(excess);
                totalExcess  = totalExcess.add(excess);
            }
        }

        BigDecimal eligible = totalExcess.max(BigDecimal.ZERO);

        return Map.of(
            "totalTaxPaid",         totalTaxPaid,
            "totalTaxLiability",    totalLiability,
            "previouslyClaimed",    BigDecimal.ZERO,
            "eligibleRefundAmount", eligible,
            "calculationBasis",     "System calculated based on tax records"
        );
    }

    // ── Mapping: ITR ──────────────────────────────────────────────────────────

    private RefundSourceResponse mapItrToSourceResponse(IncomeTaxReturn itr) {
        RefundSourceResponse r = new RefundSourceResponse();
        r.setId(itr.getId());
        r.setSourceType("ITR");
        r.setReference(itr.getReturnNo() != null ? itr.getReturnNo() : "ITR-" + itr.getId());
        r.setPeriodLabel(buildItrPeriodLabel(itr));
        r.setFiscalYear(notNull(itr.getAssessmentYear()));

        String[] dates = derivePeriodDates(itr.getIncomeYear());
        r.setPeriodStart(dates[0]);
        r.setPeriodEnd(dates[1]);

        BigDecimal paid      = computeTotalPaid(itr);
        BigDecimal liability = computeNetPayable(itr);
        r.setTaxPaid(paid);
        r.setTaxLiability(liability);
        r.setExcessAmount(paid.subtract(liability).max(BigDecimal.ZERO));

        r.setTaxpayerName(notNull(itr.getTaxpayerName()));
        r.setTin(notNull(itr.getTinNumber()));
        return r;
    }

    // ── Mapping: AIT ──────────────────────────────────────────────────────────

    private RefundSourceResponse mapAitToSourceResponse(AitRecord a) {
        RefundSourceResponse r = new RefundSourceResponse();
        r.setId(a.getId());
        r.setSourceType("AIT");
        r.setReference(a.getAitReferenceNo());

        if (a.getFiscalYear() != null) {
            r.setFiscalYear(notNull(a.getFiscalYear().getYearName()));
            r.setPeriodLabel("FY " + a.getFiscalYear().getYearName());
        }

        BigDecimal excess = computeAitExcess(a);
        r.setTaxPaid(excess);
        r.setTaxLiability(BigDecimal.ZERO);
        r.setExcessAmount(excess);

        if (a.getTaxpayer() != null) {
            r.setTaxpayerName(notNull(
                a.getTaxpayer().getFullName() != null
                    ? a.getTaxpayer().getFullName()
                    : a.getTaxpayer().getCompanyName()));
            r.setTin(notNull(a.getTaxpayer().getTinNumber()));
        }
        return r;
    }

    // ── Calculation helpers ───────────────────────────────────────────────────


    private BigDecimal computeTotalPaid(IncomeTaxReturn itr) {
        return safeD(itr.getAdvanceTaxPaid())
             .add(safeD(itr.getWithholdingTax()))
             .add(safeD(itr.getTaxPaid()));
    }


    private BigDecimal computeNetPayable(IncomeTaxReturn itr) {
        BigDecimal net = safeD(itr.getGrossTax()).subtract(safeD(itr.getTaxRebate()));
        return net.compareTo(BigDecimal.ZERO) > 0 ? net : BigDecimal.ZERO;
    }

    private BigDecimal computeAitExcess(AitRecord a) {
        if (a.getApprovedAitAmount() != null
                && a.getApprovedAitAmount().compareTo(BigDecimal.ZERO) > 0)
            return a.getApprovedAitAmount();
        return a.getCalculatedAitAmount() != null
            ? a.getCalculatedAitAmount().max(BigDecimal.ZERO)
            : BigDecimal.ZERO;
    }

    // ── String helpers ────────────────────────────────────────────────────────

    private String buildItrPeriodLabel(IncomeTaxReturn itr) {
        String fy     = notNull(itr.getAssessmentYear());
        String income = notNull(itr.getIncomeYear());
        if (!fy.isEmpty() && !income.isEmpty()) return "FY " + fy + " (" + income + ")";
        if (!fy.isEmpty()) return "FY " + fy;
        return income;
    }

    private String[] derivePeriodDates(String incomeYear) {
        if (incomeYear == null || !incomeYear.contains("-")) return new String[]{"", ""};
        try {
            String[] p   = incomeYear.split("-");
            String start = p[0].trim();
            String end   = p[1].trim().length() == 2
                ? start.substring(0, 2) + p[1].trim() : p[1].trim();
            return new String[]{start + "-07-01", end + "-06-30"};
        } catch (Exception e) { return new String[]{"", ""}; }
    }

    private BigDecimal safeD(Double v) {
        if (v == null || v.isNaN() || v.isInfinite()) return BigDecimal.ZERO;
        return BigDecimal.valueOf(v);
    }

    private String notNull(String s) { return s != null ? s : ""; }

}