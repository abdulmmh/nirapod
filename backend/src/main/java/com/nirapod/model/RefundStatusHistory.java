package com.nirapod.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "refundStatusHistory")
@Table(name = "refund_status_history")
public class RefundStatusHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "refund_id", nullable = false)
  private Refund refund;

  @Column(name = "from_status", length = 30)
  private String fromStatus;

  @Column(name = "to_status", nullable = false, length = 30)
  private String toStatus;

  @Column(name = "changed_by", nullable = false, length = 100)
  private String changedBy;

  @Column(name = "changed_by_name", length = 150)
  private String changedByName;

  @Column(name = "changed_by_role", length = 50)
  private String changedByRole;

  @Column(name = "changed_at", nullable = false)
  private LocalDateTime changedAt = LocalDateTime.now();

  @Column(name = "change_reason", length = 1000)
  private String changeReason;

  @Column(name = "amount_at_time", precision = 18, scale = 2)
  private BigDecimal amountAtTime;

  @Column(name = "ip_address", length = 45)
  private String ipAddress;

  // â”€â”€ Getters & Setters â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

  public Long getId() {
    return id;
  }

  public void setId(Long v) {
    this.id = v;
  }

  public Refund getRefund() {
    return refund;
  }

  public void setRefund(Refund v) {
    this.refund = v;
  }

  public String getFromStatus() {
    return fromStatus;
  }

  public void setFromStatus(String v) {
    this.fromStatus = v;
  }

  public String getToStatus() {
    return toStatus;
  }

  public void setToStatus(String v) {
    this.toStatus = v;
  }

  public String getChangedBy() {
    return changedBy;
  }

  public void setChangedBy(String v) {
    this.changedBy = v;
  }

  public String getChangedByName() {
    return changedByName;
  }

  public void setChangedByName(String v) {
    this.changedByName = v;
  }

  public String getChangedByRole() {
    return changedByRole;
  }

  public void setChangedByRole(String v) {
    this.changedByRole = v;
  }

  public LocalDateTime getChangedAt() {
    return changedAt;
  }

  public void setChangedAt(LocalDateTime v) {
    this.changedAt = v;
  }

  public String getChangeReason() {
    return changeReason;
  }

  public void setChangeReason(String v) {
    this.changeReason = v;
  }

  public BigDecimal getAmountAtTime() {
    return amountAtTime;
  }

  public void setAmountAtTime(BigDecimal v) {
    this.amountAtTime = v;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String v) {
    this.ipAddress = v;
  }
}
