package com.example.order_management.dto;
import lombok.Data;
@Data
public class PaymentRequest {
    private int userId;
    private int orderId;
    private double amount;

    public PaymentRequest(int orderId, double amount, int userId) {

        this.orderId = orderId;
        this.amount = amount;
        this.userId = userId;
    }

//    public PaymentRequest() {
//    }
//
//    public void setOrderId(int orderId) {
//        this.orderId = orderId;
//    }
//
//    public void setAmount(double amount) {
//        this.amount = amount;
//    }
//
//    public int getOrderId() {
//        return this.orderId;
//    }
//
//    public double getAmount() {
//        return this.amount;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getUserId() {
//        return this.userId;
//    }

}
