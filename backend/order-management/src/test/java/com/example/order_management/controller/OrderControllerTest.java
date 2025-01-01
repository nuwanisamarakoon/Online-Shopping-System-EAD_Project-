package com.example.order_management.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.order_management.dto.OrderStatusDto;
import com.example.order_management.model.Orders;
import com.example.order_management.model.Orders.Status;
import com.example.order_management.service.OrderService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void testGetAllOrders() throws Exception {
        // Arrange
        Orders order1 = new Orders(1, "Address 1", 100.0, Status.PENDING);
        Orders order2 = new Orders(2, "Address 2", 200.0, Status.PAID);
        when(orderService.getAllOrders()).thenReturn(Arrays.asList(order1, order2));

        // Act & Assert
        mockMvc.perform(get("/api/order/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[1].userId").value(2));
    }

    @Test
    void testGetOrderByOrderId() throws Exception {
        // Arrange
        Orders order = new Orders(1, "Address 1", 100.0, Status.PENDING);
        when(orderService.getOrderByOrderId(1)).thenReturn(Optional.of(order));

        // Act & Assert
        mockMvc.perform(get("/api/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.0));
    }

    @Test
    void testCreateOrder() throws Exception {
        // Arrange
        Orders newOrder = new Orders(1, "Address 1", 100.0, Status.PENDING);
        when(orderService.createOrder(Mockito.eq(1), any(Orders.class))).thenReturn(newOrder);

        // Act & Assert
        mockMvc.perform(post("/api/order/createOrder/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1,\"shippingAddress\": \"Address 1\", \"totalAmount\": 100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.shippingAddress").value("Address 1"))
                .andExpect(jsonPath("$.totalAmount").value(100.0));
    }

    @Test
    void testUpdateOrderStatusOnly() throws Exception {
        // Arrange
        Orders updatedOrder = new Orders(1, "Address 1", 100.0, Status.PAID);
        OrderStatusDto orderStatusDto = new OrderStatusDto();
        orderStatusDto.setOrderStatus("PAID");
        when(orderService.updateOrderStatusOnly(Mockito.eq(1), any(OrderStatusDto.class))).thenReturn(updatedOrder);

        // Act & Assert
        mockMvc.perform(put("/api/order/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PAID\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        // Arrange
        when(orderService.deleteOrder(1)).thenReturn("Order deleted successfully");

        // Act & Assert
        mockMvc.perform(delete("/api/order/deleteOrder")
                        .param("orderId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order deleted successfully"));
    }
}
