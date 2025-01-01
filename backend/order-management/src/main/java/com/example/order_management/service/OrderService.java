package com.example.order_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.order_management.dto.OrderStatusDto;
import com.example.order_management.dto.PaymentRequest;
import com.example.order_management.dto.PaymentResponse;
import com.example.order_management.dto.ShoppingCartItemDto;
import com.example.order_management.model.Orders;
import com.example.order_management.repository.OrderRespository;
import com.example.order_management.model.OrderItems;

@Service
public class OrderService {
    @Autowired
    private OrderRespository orderRespository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderItemsService orderItemService;

    @Autowired
    private RestTemplate restTemplate;

    public Orders createOrder(int userId, Orders order) {
        // Fetch and validate shopping cart items
        List<ShoppingCartItemDto> cartItems = shoppingCartService.getShoppingCartItemsForOrderItems(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Shopping cart is empty. Cannot proceed with the order.");
        }

        // Save the initial order
        Orders newOrder = orderRespository.save(order);
        if (newOrder == null) {
            throw new IllegalStateException("Failed to save the order.");
        }

        // Process order items
        for (ShoppingCartItemDto cartItem : cartItems) {
            createAndSaveOrderItem(cartItem, newOrder);
            deleteItemFromtheShoppingCart(cartItem.getId());
        }

        // Process payment
        PaymentRequest paymentRequest = new PaymentRequest(
                newOrder.getId(),
                newOrder.getTotalAmount(),
                newOrder.getUserId()
        );
//        System.out.println(paymentRequest.getaAmount());
        PaymentResponse paymentResponse = sendPaymentRequest(paymentRequest);

        if (paymentResponse != null && paymentResponse.getPaymentId() > 0) {
            newOrder.setPaymentId(paymentResponse.getPaymentId());
            newOrder = updateOrderStatus(newOrder.getId(), newOrder);
        } else {
            throw new IllegalStateException("Payment failed. Order remains in pending state.");
        }

        return newOrder;
    }

    private void createAndSaveOrderItem(ShoppingCartItemDto cartItem, Orders order) {
        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);
        orderItem.setItemId(cartItem.getId());
        orderItem.setQuantity(cartItem.getItemQuantity());
        orderItem.setItemName(cartItem.getItemName());
        orderItem.setItemPrice(cartItem.getItemPrice());
        orderItemService.createOrderItem(orderItem);
    }

    // Update Payment Id and Status after payment
    public Orders updateOrderStatus(int orderId, Orders order) {
        Orders orderDetails = orderRespository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderDetails.setPaymentId(order.getPaymentId());
        orderDetails.setStatus(Orders.Status.PAID);
        return orderRespository.save(orderDetails);
    }

    // Update order status only
    public Orders updateOrderStatusOnly(int orderId, OrderStatusDto order) {
        Orders orderDetails = orderRespository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        String updatedOrderStatus = order.getOrderStatus();
        if (Objects.equals(updatedOrderStatus, "ONDELIVERY")) {
            orderDetails.setStatus(Orders.Status.ONDELIVERY);
        } else if (Objects.equals(updatedOrderStatus, "COMPLETED")) {
            orderDetails.setStatus(Orders.Status.COMPLETED);
        }

        return orderRespository.save(orderDetails);
    }

    // Delete item from the shopping cart after creating the orderItem
    public void deleteItemFromtheShoppingCart(int id) {
        shoppingCartService.deleteItemFromtheShoppingCart(id);
    }

    public PaymentResponse sendPaymentRequest(PaymentRequest paymentRequest) {
        String url = "lb://PAYMENT-MANAGEMENT-SERVICE/payments"; // Replace with actual URL

        // Send POST request
        ResponseEntity<PaymentResponse> responseEntity = restTemplate.postForEntity(url, paymentRequest, PaymentResponse.class);

        // Return the response body
        return responseEntity.getBody();
    }

    public Optional<Orders> getOrderByOrderId(int orderId) {
        try {
            return orderRespository.findById(orderId);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving order with Id " + orderId, e);
        }

    }

    public List<Orders> getAllOrders() {
        try {
            return orderRespository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all orders", e);
        }
    }

    public String deleteOrder(int orderId) {
        try {
            if (orderRespository.findById(orderId) == null) {
                throw new RuntimeException("Order with ID: " + orderId + " does not exist.");
            }
            orderRespository.deleteById(orderId);
            return "Successfully Deleted";
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting order with ID: " + orderId, e);
        }
    }
}
