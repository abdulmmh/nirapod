package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class OutstandingItem {

    @SerializedName("type")
    private String type;

    @SerializedName("returnNo")
    private String returnNo;

    @SerializedName("label")
    private String label;

    @SerializedName("totalDue")
    private double totalDue;

    @SerializedName("alreadyPaid")
    private double alreadyPaid;

    @SerializedName("outstanding")
    private double outstanding;

    @SerializedName("dueDate")
    private String dueDate;

    @SerializedName("status")
    private String status;

    @SerializedName("overdue")
    private boolean overdue;

    // Getters
    public String getType()        { return type; }
    public String getReturnNo()    { return returnNo; }
    public String getLabel()       { return label; }
    public double getTotalDue()    { return totalDue; }
    public double getAlreadyPaid() { return alreadyPaid; }
    public double getOutstanding() { return outstanding; }
    public String getDueDate()     { return dueDate; }
    public String getStatus()      { return status; }
    public boolean isOverdue()     { return overdue; }
}