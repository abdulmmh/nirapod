package com.vtms.nirapod.data.model;

import java.io.Serializable;

public class AuditDocumentRequest implements Serializable {
    private long id;
    private String requestNo;
    private String requestType;
    private String status;
    private String deadline;
    private String requestedDocuments;
    private String requestReason;
    private String fulfillmentNotes;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getRequestNo() { return requestNo; }
    public void setRequestNo(String requestNo) { this.requestNo = requestNo; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getRequestedDocuments() { return requestedDocuments; }
    public void setRequestedDocuments(String requestedDocuments) { this.requestedDocuments = requestedDocuments; }
    public String getRequestReason() { return requestReason; }
    public void setRequestReason(String requestReason) { this.requestReason = requestReason; }
    public String getFulfillmentNotes() { return fulfillmentNotes; }
    public void setFulfillmentNotes(String fulfillmentNotes) { this.fulfillmentNotes = fulfillmentNotes; }
}
