package com.nirapod.dto.request;

import java.time.LocalDate;

public class AppealHearingRequest {
    private LocalDate hearingDate;
    private String hearingVenue;
    private String hearingNotes;
    private String assignedTo;

    public LocalDate getHearingDate() { return hearingDate; }
    public void setHearingDate(LocalDate hearingDate) { this.hearingDate = hearingDate; }
    public String getHearingVenue() { return hearingVenue; }
    public void setHearingVenue(String hearingVenue) { this.hearingVenue = hearingVenue; }
    public String getHearingNotes() { return hearingNotes; }
    public void setHearingNotes(String hearingNotes) { this.hearingNotes = hearingNotes; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
}
