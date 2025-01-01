package com.paymentmanagement.paymentmanagement.Service;

import com.paymentmanagement.paymentmanagement.Dto.PaymentMethodRequest;
import com.paymentmanagement.paymentmanagement.Dto.PaymentRequest;
import com.paymentmanagement.paymentmanagement.Dto.PaymentResponse;
import com.paymentmanagement.paymentmanagement.Entity.Payment;
import com.paymentmanagement.paymentmanagement.Entity.PaymentMethod;
import com.paymentmanagement.paymentmanagement.Exception.InvalidPaymentStatusException;
import com.paymentmanagement.paymentmanagement.Repository.PaymentMethodRepo;
import com.paymentmanagement.paymentmanagement.Repository.PaymentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private PaymentMethodRepo paymentMethodRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment() {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest(1, 1, 100.0);
        Payment payment = new Payment(1, 1, 150.0);
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setAmount(paymentRequest.getAmount());
        payment.setStatus(Payment.Status.PENDING);

        when(paymentRepo.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment savedPayment = invocation.getArgument(0);
            savedPayment.setId(1); // Mock database ID generation
            return savedPayment;
        });

        // When
        PaymentResponse response = paymentService.processPayment(paymentRequest);

        // Then
        assertThat(response.getOrderId()).isEqualTo(paymentRequest.getOrderId());
        assertThat(response.getAmount()).isEqualTo(paymentRequest.getAmount());
        assertThat(response.getPaymentId()).isEqualTo(1);
        verify(paymentRepo, times(1)).save(any(Payment.class));
    }


    @Test
    void testGetPaymentById() {
        // Given
        Payment payment = new Payment(1, 1, 150.0);
        payment.setId(1);
        when(paymentRepo.findById(1)).thenReturn(Optional.of(payment));

        // When
        Optional<Payment> result = paymentService.getPaymentById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        verify(paymentRepo, times(1)).findById(1);
    }

    @Test
    void testUpdatePayment() {
        // Given
        Payment existingPayment = new Payment(1, 1, 150.0);
        existingPayment.setId(1);
        existingPayment.setAmount(100.00);

        Payment updatedPayment = new Payment(1, 1, 150.0);
        updatedPayment.setAmount(200.00);

        when(paymentRepo.findById(1)).thenReturn(Optional.of(existingPayment));
        when(paymentRepo.save(existingPayment)).thenReturn(existingPayment);

        // When
        Payment result = paymentService.updatePayment(1, updatedPayment);

        // Then
        assertThat(result.getAmount()).isEqualTo(200.00);
        verify(paymentRepo, times(1)).findById(1);
        verify(paymentRepo, times(1)).save(existingPayment);
    }

    @Test
    void testConfirmPayment_Success() {
        // Given
        Payment payment = new Payment(1, 1, 150.0);
        payment.setOrderId(123);
        payment.setAmount(500.00);
        payment.setStatus(Payment.Status.PENDING);

        when(paymentRepo.findByOrderIdAndAmount(1, 500.00)).thenReturn(Optional.of(payment));
        when(paymentRepo.save(payment)).thenReturn(payment);

        // When
        Payment result = paymentService.confirmPayment(1, 500.00);

        // Then
        assertThat(result.getStatus()).isEqualTo(Payment.Status.CONFIRMED);
        verify(paymentRepo, times(1)).findByOrderIdAndAmount(1, 500.00);
        verify(paymentRepo, times(1)).save(payment);
    }

    @Test
    void testConfirmPayment_InvalidStatus() {
        // Given
        Payment payment = new Payment(1, 1, 150.0);
        payment.setOrderId(1);
        payment.setAmount(500.00);
        payment.setStatus(Payment.Status.FAILED);

        when(paymentRepo.findByOrderIdAndAmount(1, 500.00)).thenReturn(Optional.of(payment));

        // When & Then
        assertThrows(InvalidPaymentStatusException.class, () -> paymentService.confirmPayment(1, 500.00));
        verify(paymentRepo, times(1)).findByOrderIdAndAmount(1, 500.00);
    }

    @Test
    void testSavePaymentMethod() {
        // Given
        PaymentMethodRequest request = new PaymentMethodRequest(1, "John Doe", "1234567812345678", "12/26", "123", "My Card");
        PaymentMethod paymentMethod = new PaymentMethod(1, "Credit Card");
        paymentMethod.setId(1);
        when(paymentMethodRepo.save(any(PaymentMethod.class))).thenReturn(paymentMethod);

        // When
        PaymentMethod result = paymentService.savePaymentMethod(request);

        // Then
        assertThat(result.getId()).isEqualTo(1);
        verify(paymentMethodRepo, times(1)).save(any(PaymentMethod.class));
    }

    @Test
    void testGetAllOrderIdsByUserId() {
        // Given
        Payment payment1 = new Payment(1, 1, 150.0);
        payment1.setOrderId(1);
        Payment payment2 = new Payment(1, 1, 150.0);
        payment2.setOrderId(2);
        when(paymentRepo.findByUserId(1)).thenReturn(Arrays.asList(payment1, payment2));

        // When
        List<Integer> result = paymentService.getAllOrderIdsByUserId(1);

        // Then
        assertThat(result).containsExactlyInAnyOrder(1, 2);
        verify(paymentRepo, times(1)).findByUserId(1);
    }

    @Test
    void testGetPendingAndPaidDeliveryOrderIdsByUserId() {
        // Given
        Payment payment1 = new Payment(1, 1, 150.0);
        payment1.setOrderId(1);
        payment1.setStatus(Payment.Status.PENDING);
        Payment payment2 = new Payment(1, 1, 150.0);
        payment2.setOrderId(2);
        payment2.setStatus(Payment.Status.PAID);
        when(paymentRepo.findByUserIdAndStatusIn(1, Arrays.asList(Payment.Status.PENDING, Payment.Status.PAID)))
                .thenReturn(Arrays.asList(payment1, payment2));

        // When
        List<Integer> result = paymentService.getPendingAndPaidDeliveryOrderIdsByUserId(1);

        // Then
        assertThat(result).containsExactlyInAnyOrder(1, 2);
        verify(paymentRepo, times(1)).findByUserIdAndStatusIn(1, Arrays.asList(Payment.Status.PENDING, Payment.Status.PAID));
    }
}
