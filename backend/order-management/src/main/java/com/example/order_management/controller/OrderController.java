package com.example.order_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.order_management.dto.OrderStatusDto;
import com.example.order_management.model.Orders;
import com.example.order_management.service.OrderService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/")
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Optional<Orders> getOrderByOrderId(@PathVariable int orderId) {
        return orderService.getOrderByOrderId(orderId);
    }

    @PostMapping("/createOrder/{userId}")
    public Orders createOrder(@RequestBody Orders order, @PathVariable @RequestParam int userId) {
        return orderService.createOrder(userId, order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Orders> updateOrderStatusOnly(@PathVariable int orderId,
            @RequestBody OrderStatusDto order) {
        return ResponseEntity.ok(orderService.updateOrderStatusOnly(orderId, order));
    }

    @DeleteMapping("/deleteOrder")
    public String deleteOrder(@PathVariable @RequestParam int orderId) {
        String success = orderService.deleteOrder(orderId);
        return success;

    }

}
