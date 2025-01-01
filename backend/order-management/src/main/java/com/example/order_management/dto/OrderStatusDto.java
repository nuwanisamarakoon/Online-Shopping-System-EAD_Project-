package com.example.order_management.dto;

public class OrderStatusDto {
    private int orderId;
    private String orderStatus;

    public OrderStatusDto() {
    }

    public OrderStatusDto(int orderId, String orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
