package com.nirapod.service;

import com.nirapod.model.IncomeTaxReturn;
import org.springframework.stereotype.Service;

@Service
public class TaxCalculationService {


    private static final double[][] INDIVIDUAL_SLABS = {
        { 350_000, 0.00  },
        { 100_000, 0.05  },
        { 300_000, 0.10  },
        { 400_000, 0.15  },
        { 500_000, 0.20  },
        { Double.MAX_VALUE, 0.25 }
    };

    private static final double INDIVIDUAL_MIN_TAX = 2_000;


    private static final double RATE_PRIVATE_LIMITED     = 0.275;
    private static final double RATE_PUBLICLY_TRADED     = 0.225;
    private static final double RATE_PARTNERSHIP         = 0.30;
    private static final double RATE_NGO                 = 0.15;
    private static final double RATE_BANK_NBFI           = 0.375;
    private static final double RATE_MOBILE_OPERATOR     = 0.45;
    private static final double RATE_COMPANY_DEFAULT     = 0.275;


    public void calculate(IncomeTaxReturn itr) {
        double gross   = nullSafe(itr.getGrossIncome());
        double exempt  = nullSafe(itr.getExemptIncome());
        double taxable = Math.max(0, gross - exempt);

        TaxResult result = switch (normalise(itr.getItrCategory())) {
            case "individual"  -> calculateIndividual(taxable);
            case "partnership" -> calculateFlat(taxable, RATE_PARTNERSHIP);
            case "ngo"         -> calculateFlat(taxable, RATE_NGO);
            case "company"     -> calculateCompany(taxable, itr.getCompanySubType());
            default            -> calculateIndividual(taxable);
        };

        itr.setTaxRate(result.effectiveRatePct());
        itr.setGrossTax(result.grossTax());
    }


    private TaxResult calculateIndividual(double taxableIncome) {
        if (taxableIncome <= 0) {
            return new TaxResult(0, 0);
        }

        double remaining = taxableIncome;
        double tax       = 0;

        for (double[] slab : INDIVIDUAL_SLABS) {
            if (remaining <= 0) break;
            double slabAmount = Math.min(remaining, slab[0]);
            tax      += slabAmount * slab[1];
            remaining -= slabAmount;
        }

        if (taxableIncome > INDIVIDUAL_SLABS[0][0] && tax < INDIVIDUAL_MIN_TAX) {
            tax = INDIVIDUAL_MIN_TAX;
        }

        double rounded        = Math.round(tax);
        double effectiveRate  = taxableIncome > 0 ? (rounded / taxableIncome) * 100 : 0;

        return new TaxResult(round2(effectiveRate), rounded);
    }


    private TaxResult calculateCompany(double taxableIncome, String subType) {
        double rate = RATE_COMPANY_DEFAULT;

        if (subType != null) {
            rate = switch (normalise(subType)) {
                case "public limited", "publicly traded listed" -> RATE_PUBLICLY_TRADED;
                case "bank", "nbfi", "financial institution"   -> RATE_BANK_NBFI;
                case "mobile operator", "telecom"              -> RATE_MOBILE_OPERATOR;
                default                                         -> RATE_PRIVATE_LIMITED;
            };
        }

        return calculateFlat(taxableIncome, rate);
    }

    // â”€â”€ Generic flat-rate helper â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private TaxResult calculateFlat(double taxableIncome, double rate) {
        double tax = Math.round(taxableIncome * rate);
        return new TaxResult(rate * 100, tax);
    }


    private String normalise(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private double nullSafe(Double v) {
        return v != null ? v : 0.0;
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }


    public record TaxResult(double effectiveRatePct, double grossTax) {}
}
