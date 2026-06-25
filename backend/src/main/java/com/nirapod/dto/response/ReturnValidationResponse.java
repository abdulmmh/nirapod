package com.nirapod.dto.response;

/**
 * ReturnValidationResponse
 *
 * Returned by GET /api/payments/validate-return?paymentType=X&returnNo=Y
 *
 * Used by the payment-create form to show a live, non-blocking hint to the
 * user about whether the entered returnNo will actually link to a real
 * VatReturn / IncomeTaxReturn / Penalty record (and therefore whether
 * PaymentLedgerService will be able to deduct the liability once the
 * payment is marked Completed).
 *
 * found   = true  → a matching record exists; outstanding/totalDue/alreadyPaid are populated
 * warning = true  → returnNo was provided but NO matching record was found
 *                    (payment will still be accepted, but liability won't auto-update)
 * Neither found nor warning (both false) → validation not applicable
 *    (e.g. paymentType = "Other", or returnNo left blank)
 */
public class ReturnValidationResponse {

    private boolean found;
    private boolean warning;
    private String  returnNo;
    private String  paymentType;
    private String  status;
    private Double  totalDue;
    private Double  alreadyPaid;
    private Double  outstanding;
    private String  message;

    public ReturnValidationResponse() {}

    // ── Static factories ───────────────────────────────────────────────────

    /** Neither found nor a warning — validation simply not applicable. */
    public static ReturnValidationResponse neutral(String paymentType, String returnNo, String message) {
        ReturnValidationResponse r = new ReturnValidationResponse();
        r.paymentType = paymentType;
        r.returnNo    = returnNo;
        r.found       = false;
        r.warning     = false;
        r.message     = message;
        return r;
    }

    /** returnNo provided but no matching record exists. */
    public static ReturnValidationResponse notFound(String paymentType, String returnNo, String message) {
        ReturnValidationResponse r = new ReturnValidationResponse();
        r.paymentType = paymentType;
        r.returnNo    = returnNo;
        r.found       = false;
        r.warning     = true;
        r.message     = message;
        return r;
    }

    /** Matching record found — outstanding = max(0, totalDue - alreadyPaid). */
    public static ReturnValidationResponse found(String paymentType, String returnNo, String status,
                                                   double totalDue, double alreadyPaid, String message) {
        ReturnValidationResponse r = new ReturnValidationResponse();
        r.paymentType  = paymentType;
        r.returnNo     = returnNo;
        r.found        = true;
        r.warning      = false;
        r.status       = status;
        r.totalDue     = totalDue;
        r.alreadyPaid  = alreadyPaid;
        r.outstanding  = Math.max(0, totalDue - alreadyPaid);
        r.message      = message;
        return r;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public boolean isFound()              { return found; }
    public void setFound(boolean v)       { this.found = v; }

    public boolean isWarning()            { return warning; }
    public void setWarning(boolean v)     { this.warning = v; }

    public String getReturnNo()           { return returnNo; }
    public void setReturnNo(String v)     { this.returnNo = v; }

    public String getPaymentType()        { return paymentType; }
    public void setPaymentType(String v)  { this.paymentType = v; }

    public String getStatus()             { return status; }
    public void setStatus(String v)       { this.status = v; }

    public Double getTotalDue()           { return totalDue; }
    public void setTotalDue(Double v)     { this.totalDue = v; }

    public Double getAlreadyPaid()        { return alreadyPaid; }
    public void setAlreadyPaid(Double v)  { this.alreadyPaid = v; }

    public Double getOutstanding()        { return outstanding; }
    public void setOutstanding(Double v)  { this.outstanding = v; }

    public String getMessage()            { return message; }
    public void setMessage(String v)      { this.message = v; }
}