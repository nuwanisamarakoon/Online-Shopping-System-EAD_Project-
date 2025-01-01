package com.paymentmanagement.paymentmanagement.Dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private int orderId;
    private int paymentId;
    private double amount;

    public PaymentResponse(int orderId, int paymentId, double amount) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.amount = amount;
    }

}
