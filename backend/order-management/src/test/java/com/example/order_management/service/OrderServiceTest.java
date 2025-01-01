package com.example.order_management.service;

import com.example.order_management.dto.*;
import com.example.order_management.model.Orders;
import com.example.order_management.model.OrderItems;
import com.example.order_management.repository.OrderRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRespository orderRespository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private OrderItemsService orderItemsService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderSuccess() {
        int userId = 1;
        Orders order = new Orders();
        order.setId(1);
        order.setUserId(userId);
        order.setTotalAmount(100.0);

        ShoppingCartItemDto cartItem = new ShoppingCartItemDto(12, "Test Item", 50, 3,"");
        List<ShoppingCartItemDto> cartItems = List.of(cartItem);

        PaymentRequest paymentRequest = new PaymentRequest(order.getId(), order.getTotalAmount(), order.getUserId());
        PaymentResponse paymentResponse = new PaymentResponse(order.getId(), 2, order.getTotalAmount());

        // Mock dependencies
        when(shoppingCartService.getShoppingCartItemsForOrderItems(userId)).thenReturn(cartItems);
        when(orderRespository.save(order)).thenReturn(order);
        when(restTemplate.postForEntity(anyString(), eq(paymentRequest), eq(PaymentResponse.class)))
                .thenReturn(ResponseEntity.ok(paymentResponse));
        when(orderRespository.findById(order.getId())).thenReturn(Optional.of(order));
        doNothing().when(shoppingCartService).deleteItemFromtheShoppingCart(cartItem.getId());

        // Execute the method
        Orders createdOrder = orderService.createOrder(userId, order);

        // Verify interactions and assertions
        verify(orderRespository, times(2)).save(order);
        verify(orderItemsService, times(1)).createOrderItem(any(OrderItems.class));
        verify(shoppingCartService, times(1)).deleteItemFromtheShoppingCart(cartItem.getId());

        assertNotNull(createdOrder);
        assertEquals(Orders.Status.PAID, createdOrder.getStatus());
        assertEquals(paymentResponse.getPaymentId(), createdOrder.getPaymentId());
    }

    @Test
    void testCreateOrderWithEmptyCart() {
        int userId = 1;
        Orders order = new Orders();

        when(shoppingCartService.getShoppingCartItemsForOrderItems(userId)).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(userId, order)
        );

        assertEquals("Shopping cart is empty. Cannot proceed with the order.", exception.getMessage());
    }

    @Test
    void testUpdateOrderStatus() {
        int orderId = 1;
        Orders order = new Orders();
        order.setId(orderId);
        order.setPaymentId(123);

        Orders existingOrder = new Orders();
        existingOrder.setId(orderId);

        when(orderRespository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRespository.save(existingOrder)).thenReturn(existingOrder);

        Orders updatedOrder = orderService.updateOrderStatus(orderId, order);

        assertNotNull(updatedOrder);
        assertEquals(Orders.Status.PAID, updatedOrder.getStatus());
        assertEquals(order.getPaymentId(), updatedOrder.getPaymentId());
    }

    @Test
    void testDeleteOrder() {
        int orderId = 1;
        Orders order = new Orders();
        order.setId(orderId);

        when(orderRespository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderRespository).deleteById(orderId);

        String result = orderService.deleteOrder(orderId);

        assertEquals("Successfully Deleted", result);
        verify(orderRespository, times(1)).deleteById(orderId);
    }

    @Test
    void testDeleteOrderNotFound() {
        int orderId = 1;

        when(orderRespository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.deleteOrder(orderId)
        );

        assertEquals("Order with ID: 1 does not exist.", exception.getMessage());
    }
}
