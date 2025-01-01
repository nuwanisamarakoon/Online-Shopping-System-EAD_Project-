package com.paymentmanagement.paymentmanagement.Repository;

import com.paymentmanagement.paymentmanagement.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Integer> {
    Optional<Payment> findById(int id);

    void deleteById(int id);
    // Find a payment by order ID and amount
    Optional<Payment> findByOrderIdAndAmount(int orderId, double amount);

    // Find all payments for a given user ID
    List<Payment> findByUserId(int userId);

    // Find all payments for a given user ID and specific statuses
    List<Payment> findByUserIdAndStatusIn(int userId, List<Payment.Status> statuses);
}
