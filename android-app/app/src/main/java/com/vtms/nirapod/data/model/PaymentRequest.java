package com.vtms.nirapod.data.model;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {

    @SerializedName("amount")
    private double amount;

    @SerializedName("paymentType")
    private String paymentType;

    @SerializedName("paymentMethod")
    private String paymentMethod;

    @SerializedName("description")
    private String description;

    public PaymentRequest(double amount, String paymentType,
                          String paymentMethod, String description) {
        this.amount        = amount;
        this.paymentType   = paymentType;
        this.paymentMethod = paymentMethod;
        this.description   = description;
    }
}