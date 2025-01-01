package com.example.order_management.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.order_management.model.OrderItems;
import com.example.order_management.model.Orders;
import com.example.order_management.model.Orders.Status;
import com.example.order_management.repository.OrderRespository;

@DataJpaTest
public class OrderItemsRepositoryTest {

    @Autowired
    private OrderItemsRepository underTestOrderItemsRepository;

    @Autowired
    private OrderRespository underTestOrdersRepository;

    private Orders testOrder;
    private OrderItems testOrderItem;

    @BeforeEach
    void setUp() {
        // Create and save an order
        testOrder = new Orders();
        testOrder.setUserId(1);
        testOrder.setShippingAddress("123 Test Street");
        testOrder.setTotalAmount(150.0);
        testOrder.setStatus(Status.PENDING);
        underTestOrdersRepository.save(testOrder);

        // Create and save an order item associated with the order
        testOrderItem = new OrderItems();
        testOrderItem.setItemId(101);
        testOrderItem.setQuantity(2);
        testOrderItem.setOrder(testOrder); // Associate with the testOrder
        testOrderItem.setItemName("Test Item");
        testOrderItem.setItemPrice(50.0);
        underTestOrderItemsRepository.save(testOrderItem);
    }

    @Test
    void testGetOrderItemsByOrderId() {
        // Test retrieving order items by order ID
        List<OrderItems> foundOrderItems = underTestOrderItemsRepository.getOrderItemsByOrderId(testOrder.getId());

        assertEquals(1, foundOrderItems.size(), "Only one order item should be found for the given order ID");
        assertEquals(testOrderItem.getItemName(), foundOrderItems.get(0).getItemName(), "Order item name should match");
    }

    @Test
    void testGetOrderItemsByOrderIdWhenNoItemsExist() {
        // Test retrieving order items for a non-existent order ID
        List<OrderItems> foundOrderItems = underTestOrderItemsRepository.getOrderItemsByOrderId(999);

        assertTrue(foundOrderItems.isEmpty(), "No order items should be found for a non-existent order ID");
    }

    @Test
    void testGetOrderItemsByOrderIdWhenMultipleItemsExist() {
        // Create and save another order item for the same order
        OrderItems anotherOrderItem = new OrderItems();
        anotherOrderItem.setItemId(102);
        anotherOrderItem.setQuantity(1);
        anotherOrderItem.setOrder(testOrder); // Associate with the same testOrder
        anotherOrderItem.setItemName("Another Test Item");
        anotherOrderItem.setItemPrice(30.0);
        underTestOrderItemsRepository.save(anotherOrderItem);

        // Test retrieving multiple order items by order ID
        List<OrderItems> foundOrderItems = underTestOrderItemsRepository.getOrderItemsByOrderId(testOrder.getId());

        assertEquals(2, foundOrderItems.size(), "Two order items should be found for the given order ID");
    }
}
