package com.vtms.nirapod.data.model;

import java.io.Serializable;

public class AuditQuery implements Serializable {
    private long id;
    private String queryNo;
    private String queryType;
    private String subject;
    private String queryText;
    private String status;
    private String deadline;
    private String responseText;
    private String respondedAt;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getQueryNo() { return queryNo; }
    public void setQueryNo(String queryNo) { this.queryNo = queryNo; }
    public String getQueryType() { return queryType; }
    public void setQueryType(String queryType) { this.queryType = queryType; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getQueryText() { return queryText; }
    public void setQueryText(String queryText) { this.queryText = queryText; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getResponseText() { return responseText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }
    public String getRespondedAt() { return respondedAt; }
    public void setRespondedAt(String respondedAt) { this.respondedAt = respondedAt; }
}
