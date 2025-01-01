package com.paymentmanagement.paymentmanagement.Controller;

import com.paymentmanagement.paymentmanagement.Dto.PaymentMethodRequest;
import com.paymentmanagement.paymentmanagement.Dto.PaymentRequest;
import com.paymentmanagement.paymentmanagement.Dto.PaymentResponse;
import com.paymentmanagement.paymentmanagement.Entity.Payment;
import com.paymentmanagement.paymentmanagement.Entity.PaymentMethod;
import com.paymentmanagement.paymentmanagement.Service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testProcessPayment() {
        // Given
        PaymentRequest paymentRequest = new PaymentRequest(5, 2, 100.0);
        PaymentResponse paymentResponse = new PaymentResponse(2, 1, 100.00);
        when(paymentService.processPayment(paymentRequest)).thenReturn(paymentResponse);

        // When
        ResponseEntity<PaymentResponse> response = paymentController.processPayment(paymentRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(paymentResponse);
        verify(paymentService, times(1)).processPayment(paymentRequest);
    }

    @Test
    void testGetPaymentById() {
        // Given
        Payment payment = new Payment(1, 1, 150.0);
        payment.setId(1);
        when(paymentService.getPaymentById(1)).thenReturn(Optional.of(payment));

        // When
        ResponseEntity<Payment> response = paymentController.getPaymentById(1);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(payment);
        verify(paymentService, times(1)).getPaymentById(1);
    }

    @Test
    void testGetPaymentById_NotFound() {
        // Given
        when(paymentService.getPaymentById(1)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Payment> response = paymentController.getPaymentById(1);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(paymentService, times(1)).getPaymentById(1);
    }

    @Test
    void testUpdatePayment() {
        // Given
        Payment updatedPayment = new Payment(1, 1, 200.00);
        when(paymentService.updatePayment(eq(1), any(Payment.class))).thenReturn(updatedPayment);

        // When
        ResponseEntity<Payment> response = paymentController.updatePayment(1, updatedPayment);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(updatedPayment);
        verify(paymentService, times(1)).updatePayment(eq(1), any(Payment.class));
    }

    @Test
    void testConfirmPayment() {
        // Given
        Payment confirmedPayment = new Payment(1, 1, 500.00);
        confirmedPayment.setStatus(Payment.Status.CONFIRMED);
        when(paymentService.confirmPayment(1, 500.00)).thenReturn(confirmedPayment);

        // When
        ResponseEntity<Payment> response = paymentController.confirmPayment(1, 500.00);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(confirmedPayment);
        verify(paymentService, times(1)).confirmPayment(1, 500.00);
    }

    @Test
    void testSavePaymentMethod() {
        // Given
        PaymentMethodRequest request = new PaymentMethodRequest(1, "John Doe", "1234567812345678", "12/26", "123", "My Card");
        PaymentMethod paymentMethod = new PaymentMethod(1, "Credit Card");
        when(paymentService.savePaymentMethod(request)).thenReturn(paymentMethod);

        // When
        ResponseEntity<PaymentMethod> response = paymentController.savePaymentMethod(request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(paymentMethod);
        verify(paymentService, times(1)).savePaymentMethod(request);
    }

    @Test
    void testGetAllOrderIdsByUserId() {
        // Given
        List<Integer> orderIds = Arrays.asList(1, 2);
        when(paymentService.getAllOrderIdsByUserId(1)).thenReturn(orderIds);

        // When
        ResponseEntity<List<Integer>> response = paymentController.getAllOrderIdsByUserId(1);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(1, 2);
        verify(paymentService, times(1)).getAllOrderIdsByUserId(1);
    }

    @Test
    void testGetPendingAndPaidDeliveryOrderIdsByUserId() {
        // Given
        List<Integer> orderIds = Arrays.asList(1, 2);
        when(paymentService.getPendingAndPaidDeliveryOrderIdsByUserId(1)).thenReturn(orderIds);

        // When
        ResponseEntity<List<Integer>> response = paymentController.getPendingAndPaidDeliveryOrderIdsByUserId(1);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactlyInAnyOrder(1, 2);
        verify(paymentService, times(1)).getPendingAndPaidDeliveryOrderIdsByUserId(1);
    }
}
