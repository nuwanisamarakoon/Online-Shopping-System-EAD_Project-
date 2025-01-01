package com.paymentmanagement.paymentmanagement.Repository;

import com.paymentmanagement.paymentmanagement.Entity.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepoTest {

    @Autowired
    private PaymentRepo paymentRepo;

    @Test
    void testSaveAndFindById() {
        // Given
        Payment payment = new Payment();
        payment.setUserId(1);
        payment.setOrderId(2);
        payment.setAmount(150.50);
        payment.setCurrency("USD");
        payment.setStatus(Payment.Status.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        // When
        Payment savedPayment = paymentRepo.save(payment);

        // Then
        assertThat(savedPayment.getId()).isNotEqualTo(0);
        assertThat(savedPayment.getOrderId()).isEqualTo(2);

        Optional<Payment> fetchedPayment = paymentRepo.findById(savedPayment.getId());
        assertThat(fetchedPayment).isPresent();
        assertThat(fetchedPayment.get().getAmount()).isEqualTo(150.50);
    }

    @Test
    void testDeleteById() {
        // Given
        Payment payment = new Payment();
        payment.setUserId(1);
        payment.setOrderId(101);
        payment.setAmount(200.75);
        payment.setCurrency("USD");
        payment.setStatus(Payment.Status.CONFIRMED);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepo.save(payment);

        // When
        paymentRepo.deleteById(savedPayment.getId());

        // Then
        Optional<Payment> fetchedPayment = paymentRepo.findById(savedPayment.getId());
        assertThat(fetchedPayment).isEmpty();
    }

    @Test
    void testFindByOrderIdAndAmount() {
        // Given
        Payment payment = new Payment();
        payment.setUserId(2);
        payment.setOrderId(102);
        payment.setAmount(300.00);
        payment.setCurrency("EUR");
        payment.setStatus(Payment.Status.PAID);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepo.save(payment);

        // When
        Optional<Payment> fetchedPayment = paymentRepo.findByOrderIdAndAmount(102, 300.00);

        // Then
        assertThat(fetchedPayment).isPresent();
        assertThat(fetchedPayment.get().getCurrency()).isEqualTo("EUR");
    }

    @Test
    void testFindByUserId() {
        // Given
        Payment payment1 = new Payment();
        payment1.setUserId(3);
        payment1.setOrderId(201);
        payment1.setAmount(50.00);
        payment1.setCurrency("GBP");
        payment1.setStatus(Payment.Status.FAILED);
        payment1.setCreatedAt(LocalDateTime.now());
        payment1.setUpdatedAt(LocalDateTime.now());

        Payment payment2 = new Payment();
        payment2.setUserId(3);
        payment2.setOrderId(202);
        payment2.setAmount(75.00);
        payment2.setCurrency("GBP");
        payment2.setStatus(Payment.Status.PAID);
        payment2.setCreatedAt(LocalDateTime.now());
        payment2.setUpdatedAt(LocalDateTime.now());

        paymentRepo.save(payment1);
        paymentRepo.save(payment2);

        // When
        List<Payment> payments = paymentRepo.findByUserId(3);

        // Then
        assertThat(payments).hasSize(2);
    }

    @Test
    void testFindByUserIdAndStatusIn() {
        // Given
        Payment payment = new Payment();
        payment.setUserId(4);
        payment.setOrderId(301);
        payment.setAmount(500.00);
        payment.setCurrency("USD");
        payment.setStatus(Payment.Status.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        paymentRepo.save(payment);

        // When
        List<Payment> payments = paymentRepo.findByUserIdAndStatusIn(4, List.of(Payment.Status.PENDING, Payment.Status.CONFIRMED));

        // Then
        assertThat(payments).hasSize(1);
        assertThat(payments.getFirst().getOrderId()).isEqualTo(301);
    }
}
