package com.example.order_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.order_management.model.OrderItems;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    @Query("SELECT i FROM OrderItems i WHERE i.order.id = :orderId")
    List<OrderItems> getOrderItemsByOrderId(@Param("orderId") int orderId);
}
