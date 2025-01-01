package com.example.order_management.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.order_management.model.Orders;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRespository underTestOrderRepository;

    private Orders underTestOrder;

    @BeforeEach
    void setUp() {
        // Prepare test data
        underTestOrder = new Orders();
        underTestOrder.setId(1);
        underTestOrder.setUserId(4);
        underTestOrder.setTotalAmount(200.0);
        underTestOrder.setStatus(Orders.Status.PENDING);

        // Save the order to the repository
        underTestOrderRepository.save(underTestOrder);
    }

    @Test
    void testFindById() {
        // Test that the order can be found by its ID
        Optional<Orders> foundOrder = underTestOrderRepository.findById(underTestOrder.getId());

        assertTrue(foundOrder.isPresent(), "Order should be found for the given ID");
        assertEquals(underTestOrder.getId(), foundOrder.get().getId(), "Found order ID should match the test order ID");
    }

    @Test
    void testFindByIdWhenOrderDoesNotExist() {
        // Test when no order exists for a given ID
        Optional<Orders> foundOrder = underTestOrderRepository.findById(999); // Use a non-existent order ID

        assertFalse(foundOrder.isPresent(), "Order should not be found for a non-existent ID");
    }

    @Test
    void testFindAll() {
        // Create and save another order
        Orders anotherOrder = new Orders();
        anotherOrder.setId(2);
        anotherOrder.setUserId(5);
        anotherOrder.setTotalAmount(300.0);
        anotherOrder.setStatus(Orders.Status.PENDING);
        underTestOrderRepository.save(anotherOrder);

        // Test that all orders are retrieved
        List<Orders> allOrders = underTestOrderRepository.findAll();

        assertEquals(2, allOrders.size(), "Two orders should be found in the repository");
    }

    @Test
    void testFindByUserId() {
        // Test that the order can be found by the user ID
        Optional<Orders> foundOrders = underTestOrderRepository.findById(underTestOrder.getUserId());

        assertFalse(foundOrders.isEmpty(), "Orders should be found for the given user ID");
        assertEquals(1, foundOrders.stream().count(), "Only one order should be found for the user");
        assertEquals(underTestOrder.getUserId(), foundOrders.get().getUserId(), "Found order user ID should match");
    }

    @Test
    void testFindByUserIdWhenNoOrdersExist() {
        // Test when no orders exist for a given user ID
        Optional<Orders> foundOrders = underTestOrderRepository.findById(999); // Use a non-existent user ID

        assertTrue(foundOrders.isEmpty(), "Orders should be empty for a non-existent user ID");
    }
}
