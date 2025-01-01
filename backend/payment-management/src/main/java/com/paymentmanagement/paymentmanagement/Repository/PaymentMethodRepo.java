package com.paymentmanagement.paymentmanagement.Repository;

import com.paymentmanagement.paymentmanagement.Entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepo extends JpaRepository<PaymentMethod, Integer> {
}
