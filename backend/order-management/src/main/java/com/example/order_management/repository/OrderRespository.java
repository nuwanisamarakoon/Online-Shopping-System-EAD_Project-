package com.example.order_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.order_management.model.Orders;

@Repository
public interface OrderRespository extends JpaRepository<Orders, Integer> {

}
