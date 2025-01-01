package com.example.order_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.order_management.model.OrderItems;

import com.example.order_management.service.OrderItemsService;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemsController {
    @Autowired
    OrderItemsService orderItemsService;

    @PostMapping
    public OrderItems createOrderItem(@RequestBody OrderItems orderItems) {
        return orderItemsService.createOrderItem(orderItems);
    }

    @GetMapping("/{orderId}")
    public List<OrderItems> getOrderItemsByOrderId(@PathVariable int orderId) {
        return orderItemsService.getOrderItemsByOrderId(orderId);
    }
}
