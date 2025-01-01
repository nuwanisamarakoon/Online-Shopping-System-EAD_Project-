package com.paymentmanagement.paymentmanagement.Dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private int userId;
    private int orderId;
    private double amount;

    public PaymentRequest(int userId, int orderId, double amount) {
    }

//    public PaymentRequest(int orderId, double amount, int userId) {
//        this.orderId = orderId;
//        this.amount = amount;
//        this.userId = userId;
//    }
//
//    public PaymentRequest(int i, BigDecimal bigDecimal, String creditCard) {
//    }
//
//    public PaymentRequest(int i, double v, String creditCard) {
//    }
}
