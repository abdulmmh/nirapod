package com.nirapod.dto.response;

import com.nirapod.model.Notice;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NoticeResponse {

  private Long id;
  private Long taxpayerId;
  private String taxpayerName;
  private String tinNumber;
  private String noticeNo;
  private String subject;
  private String body;
  private String noticeType;
  private String priority;
  private String targetType;
  private String issuedBy;
  private LocalDate issuedDate;
  private LocalDate dueDate;
  private LocalDate readDate;
  private LocalDate responseDate;
  private String responseNote;
  private String attachmentName;
  private String status;
  private LocalDateTime createdAt;

  public static NoticeResponse from(Notice e) {
    NoticeResponse r = new NoticeResponse();
    r.id = e.getId();
    r.noticeNo = e.getNoticeNo();
    r.subject = e.getSubject();
    r.body = e.getBody();
    r.noticeType = e.getNoticeType();
    r.priority = e.getPriority();
    r.targetType = e.getTargetType();
    r.issuedBy = e.getIssuedBy();
    r.issuedDate = e.getIssuedDate();
    r.dueDate = e.getDueDate();
    r.readDate = e.getReadDate();
    r.responseDate = e.getResponseDate();
    r.responseNote = e.getResponseNote();
    r.attachmentName = e.getAttachmentName();
    r.status = e.getStatus();
    r.createdAt = e.getCreatedAt();

    if (e.getTaxpayer() != null) {
      r.taxpayerId = e.getTaxpayer().getId();
      r.taxpayerName = e.getTaxpayer().getFullName() != null
          ? e.getTaxpayer().getFullName()
          : e.getTaxpayer().getCompanyName();
      r.tinNumber = e.getTaxpayer().getTinNumber();
    }
    return r;
  }

  // Getters
  public Long getId() {
    return id;
  }

  public Long getTaxpayerId() {
    return taxpayerId;
  }

  public String getTaxpayerName() {
    return taxpayerName;
  }

  public String getTinNumber() {
    return tinNumber;
  }

  public String getNoticeNo() {
    return noticeNo;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getNoticeType() {
    return noticeType;
  }

  public String getPriority() {
    return priority;
  }

  public String getTargetType() {
    return targetType;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public LocalDate getIssuedDate() {
    return issuedDate;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public LocalDate getReadDate() {
    return readDate;
  }

  public LocalDate getResponseDate() {
    return responseDate;
  }

  public String getResponseNote() {
    return responseNote;
  }

  public String getAttachmentName() {
    return attachmentName;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}