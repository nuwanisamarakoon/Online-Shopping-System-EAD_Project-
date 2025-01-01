package com.example.order_management.service;

import java.util.List;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.order_management.exception.OrderItemsRetrievalFailException;
import com.example.order_management.model.OrderItems;

import com.example.order_management.repository.OrderItemsRepository;

@Service
public class OrderItemsService {
    @Autowired
    private OrderItemsRepository orderItemRepository;

    public OrderItems createOrderItem(OrderItems orderItems) {
        return orderItemRepository.save(orderItems);
    }

    public List<OrderItems> getOrderItemsByOrderId(int orderId) {
        try {
            List<OrderItems> orderItems = orderItemRepository.getOrderItemsByOrderId(orderId);
            if (orderItems == null || orderItems.isEmpty()) {
                throw new ResourceNotFoundException("No order items found for order ID: " + orderId);
            }
            return orderItems;
        } catch (Exception e) {
            // Log and rethrow as a custom exception
            throw new OrderItemsRetrievalFailException("Failed to retrieve order items for order ID: " + orderId);
        }
    }

}
