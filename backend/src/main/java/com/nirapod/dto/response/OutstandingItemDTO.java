package com.nirapod.dto.response;


public class OutstandingItemDTO {

    private String type;

    private String returnNo;

    private String label;

    private double totalDue;

    private double alreadyPaid;

    private double outstanding;

    private String dueDate;

    private String status;

    private boolean overdue;

    public OutstandingItemDTO() {}

    public OutstandingItemDTO(String type, String returnNo, String label,
                               double totalDue, double alreadyPaid,
                               String dueDate, String status, boolean overdue) {
        this.type        = type;
        this.returnNo    = returnNo;
        this.label       = label;
        this.totalDue    = totalDue;
        this.alreadyPaid = alreadyPaid;
        this.outstanding = Math.max(0, totalDue - alreadyPaid);
        this.dueDate     = dueDate;
        this.status      = status;
        this.overdue     = overdue;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public String getType()              { return type; }
    public void setType(String v)        { this.type = v; }

    public String getReturnNo()          { return returnNo; }
    public void setReturnNo(String v)    { this.returnNo = v; }

    public String getLabel()             { return label; }
    public void setLabel(String v)       { this.label = v; }

    public double getTotalDue()          { return totalDue; }
    public void setTotalDue(double v)    { this.totalDue = v; }

    public double getAlreadyPaid()       { return alreadyPaid; }
    public void setAlreadyPaid(double v) { this.alreadyPaid = v; }

    public double getOutstanding()       { return outstanding; }
    public void setOutstanding(double v) { this.outstanding = v; }

    public String getDueDate()           { return dueDate; }
    public void setDueDate(String v)     { this.dueDate = v; }

    public String getStatus()            { return status; }
    public void setStatus(String v)      { this.status = v; }

    public boolean isOverdue()           { return overdue; }
    public void setOverdue(boolean v)    { this.overdue = v; }
}
