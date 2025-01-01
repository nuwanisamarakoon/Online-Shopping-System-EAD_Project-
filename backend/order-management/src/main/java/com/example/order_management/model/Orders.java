package com.example.order_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;

@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int paymentId;
    private String shippingAddress;
    @Enumerated(EnumType.STRING)
    private Status status;
    private double totalAmount;

    public Orders() {
    }

    public Orders(int userId, String shippingAddress, double totalAmount, Status status) {
        this.userId = userId;
        this.status = Orders.Status.PENDING;
        this.shippingAddress = shippingAddress;
        this.paymentId = 0;
        this.totalAmount = totalAmount;
    }

    public enum Status {
        PENDING,
        PAID,
        ONDELIVERY,
        COMPLETED, order
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress() {
        return this.shippingAddress;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public int getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

}
