package com.paymentmanagement.paymentmanagement.Service;

import com.paymentmanagement.paymentmanagement.Dto.PaymentMethodRequest;
import com.paymentmanagement.paymentmanagement.Dto.PaymentRequest;
import com.paymentmanagement.paymentmanagement.Dto.PaymentResponse;
import com.paymentmanagement.paymentmanagement.Entity.Payment;
import com.paymentmanagement.paymentmanagement.Entity.PaymentMethod;
import com.paymentmanagement.paymentmanagement.Exception.InvalidPaymentStatusException;
import com.paymentmanagement.paymentmanagement.Exception.PaymentNotFoundException;
import com.paymentmanagement.paymentmanagement.Repository.PaymentMethodRepo;
import com.paymentmanagement.paymentmanagement.Repository.PaymentRepo;
import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@CommonsLog
@Service
public class PaymentService {

    @Autowired
    private PaymentRepo paymentRepository;
    @Autowired
    private PaymentMethodRepo paymentMethodRepo;

    // Method to handle a new payment request
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {

        // Create a new Payment entity
        Payment payment = new Payment(1, 1, 150.0);
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency("USD");
        payment.setStatus(Payment.Status.PENDING);
        payment.setCreatedAt(LocalDate.now().atStartOfDay());

        // Save to the database
        paymentRepository.save(payment);

        // Return a response
        return new PaymentResponse(payment.getOrderId(), payment.getId(), payment.getAmount());
    }

    // Method to retrieve a payment by ID
    public Optional<Payment> getPaymentById(int id) {
        return paymentRepository.findById(id);
    }

    // Update an existing payment
    public Payment updatePayment(int id, Payment updatedPayment) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setAmount(updatedPayment.getAmount());
        payment.setCurrency(updatedPayment.getCurrency());
        payment.setStatus(updatedPayment.getStatus());
        return paymentRepository.save(payment);
    }

    // Delete a payment
    public void deletePayment(int id) {
        paymentRepository.deleteById(id);
    }

    // Confirm a payment by orderId and amount
    @Transactional
    public Payment confirmPayment(int orderId, double amount) {
        // Validate input
        if (orderId <= 0 || amount <= 0) {
            throw new IllegalArgumentException("Order ID and amount must be greater than 0");
        }

        // Find the payment
        Payment payment = paymentRepository.findByOrderIdAndAmount(orderId, amount)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with matching orderId and amount"));

        // Check the payment status
        if (!Payment.Status.PENDING.equals(payment.getStatus())) {
            throw new InvalidPaymentStatusException("Payment cannot be confirmed as it is not in PENDING status");
        }

        // Update and save the payment status
        payment.setStatus(Payment.Status.CONFIRMED);
        return paymentRepository.save(payment);
    }

    // Retrieve all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Save a payment method for a user
    public PaymentMethod savePaymentMethod(PaymentMethodRequest paymentMethodRequest) {
        PaymentMethod paymentMethod = new PaymentMethod(1, "John Doe", "1234567812345678", "12/26", "123", "Personal Card");
        paymentMethod.setUserId(paymentMethodRequest.getUserId());
        paymentMethod.setCardHolderName(paymentMethodRequest.getCardHolderName());
        paymentMethod.setCardNumber(paymentMethodRequest.getCardNumber());
        paymentMethod.setExpirationDate(paymentMethodRequest.getExpirationDate());
        paymentMethod.setCvv(paymentMethodRequest.getCvv());
        paymentMethod.setNickname(paymentMethodRequest.getNickname());

        return paymentMethodRepo.save(paymentMethod);
    }

    // Get all order IDs for the given user ID
    public List<Integer> getAllOrderIdsByUserId(int userId) {
        List<Payment> payments = paymentRepository.findByUserId(userId);
        if (payments == null || payments.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if no payments are found.
        }
        return payments.stream().map(Payment::getOrderId).collect(Collectors.toList());
    }

    // Get pending and paid delivery order IDs for the given user ID
    public List<Integer> getPendingAndPaidDeliveryOrderIdsByUserId(int userId) {
        List<Payment> payments = paymentRepository.findByUserIdAndStatusIn(
                userId, Arrays.asList(Payment.Status.PENDING, Payment.Status.PAID));
        if (payments == null || payments.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if no payments are found.
        }
        return payments.stream().map(Payment::getOrderId).collect(Collectors.toList());
    }

}
