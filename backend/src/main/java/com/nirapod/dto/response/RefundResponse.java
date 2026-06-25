package com.nirapod.dto.response;

import com.nirapod.model.Refund;

public class RefundResponse {

  private Long id;
  private Long taxpayerId;
  private String taxpayerName;
  private String tinNumber;
  private Refund entity;

  public static RefundResponse from(Refund e) {
    RefundResponse r = new RefundResponse();
    Number rawId = (Number) e.getId();
    r.id = rawId.longValue();
    if (e.getTaxpayer() != null) {
      r.taxpayerId = e.getTaxpayer().getId();
      r.taxpayerName = e.getTaxpayer().getFullName() != null
          ? e.getTaxpayer().getFullName()
          : e.getTaxpayer().getCompanyName();
      r.tinNumber = e.getTaxpayer().getTinNumber();
    }
    r.entity = e;
    return r;
  }

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

  public Refund getRefund() {
    return entity;
  }

  public String getStatus() {
    return entity != null ? entity.getStatus() : null;
  }
}
