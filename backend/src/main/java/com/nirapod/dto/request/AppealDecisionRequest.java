package com.nirapod.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class AppealDecisionRequest {

    @NotBlank(message = "Decision is required")
    private String decision; // UPHELD, PARTIALLY_UPHELD, DISMISSED

    private String decisionNotes;
    private BigDecimal reliefGranted;
    private BigDecimal acceptedAmount;

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getDecisionNotes() { return decisionNotes; }
    public void setDecisionNotes(String decisionNotes) { this.decisionNotes = decisionNotes; }
    public BigDecimal getReliefGranted() { return reliefGranted; }
    public void setReliefGranted(BigDecimal reliefGranted) { this.reliefGranted = reliefGranted; }
    public BigDecimal getAcceptedAmount() { return acceptedAmount; }
    public void setAcceptedAmount(BigDecimal acceptedAmount) { this.acceptedAmount = acceptedAmount; }
}
