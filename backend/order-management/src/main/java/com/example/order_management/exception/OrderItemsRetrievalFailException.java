package com.example.order_management.exception;

public class OrderItemsRetrievalFailException extends RuntimeException {
    public OrderItemsRetrievalFailException(String message) {
        super(message);
    }
}
