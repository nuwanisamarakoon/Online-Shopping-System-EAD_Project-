package com.example.order_management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.order_management.model.OrderItems;
import com.example.order_management.model.Orders;
import com.example.order_management.repository.OrderItemsRepository;

public class OrderItemsServiceTest {

    @Mock
    private OrderItemsRepository underTestOrderItemRepository;

    @InjectMocks
    private OrderItemsService underTestOrderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrderItem() {
        Orders order = new Orders();
        order.setId(1);
        order.setShippingAddress("Lorem-Ipsum");
        order.setPaymentId(3);
        order.setStatus(Orders.Status.PAID);
        order.setTotalAmount(70.45);
        order.setUserId(6);

        OrderItems underTestOrderItems = new OrderItems();

        underTestOrderItems.setId(2);
        underTestOrderItems.setItemId(3);
        underTestOrderItems.setOrder(order);
        underTestOrderItems.setQuantity(3);
        underTestOrderItems.setItemName("T-shirt");
        underTestOrderItems.setItemPrice(30.00);

        when(underTestOrderItemRepository.save(underTestOrderItems)).thenReturn(underTestOrderItems);

        OrderItems result = underTestOrderItemService.createOrderItem(underTestOrderItems);
        assertNotNull(result);
        assertEquals(3, result.getItemId()); // Ensure the itemId is correct
        assertEquals(3, result.getQuantity()); // Ensure the quantity is correct
        assertEquals(order.getId(), result.getOrder().getId()); // Ensure the order is linked correctly

        verify(underTestOrderItemRepository, times(1)).save(underTestOrderItems);

    }
}
