package com.paymentmanagement.paymentmanagement.Repository;

import com.paymentmanagement.paymentmanagement.Entity.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentMethodRepoTest {

    @Autowired
    private PaymentMethodRepo paymentMethodRepo;

    @Test
    void testSaveAndFindById() {
        // Given
        PaymentMethod paymentMethod = new PaymentMethod(
                1,
                "John Doe",
                "1234567812345678",
                "12/26",
                "123",
                "Personal Card"
        );

        // When
        PaymentMethod savedPaymentMethod = paymentMethodRepo.save(paymentMethod);

        // Then
        assertThat(savedPaymentMethod.getUserId()).isEqualTo(1);
        assertThat(savedPaymentMethod.getCardHolderName()).isEqualTo("John Doe");
        assertThat(savedPaymentMethod.getNickname()).isEqualTo("Personal Card");

        // Fetch by ID
        Optional<PaymentMethod> fetchedPaymentMethod = paymentMethodRepo.findById(savedPaymentMethod.getId());
        assertThat(fetchedPaymentMethod).isPresent();
        assertThat(fetchedPaymentMethod.get().getCardHolderName()).isEqualTo("John Doe");
    }

    @Test
    void testDeleteById() {
        // Given
        PaymentMethod paymentMethod = new PaymentMethod(
                1,
                "John Doe",
                "1234567812345678",
                "12/26",
                "123",
                "Personal Card"
        );
        PaymentMethod savedPaymentMethod = paymentMethodRepo.save(paymentMethod);

        // When
        paymentMethodRepo.deleteById(savedPaymentMethod.getId());

        // Then
        Optional<PaymentMethod> fetchedPaymentMethod = paymentMethodRepo.findById(savedPaymentMethod.getId());
        assertThat(fetchedPaymentMethod).isEmpty();
    }

    @Test
    void testFindAll() {
        // Given
        PaymentMethod method1 = new PaymentMethod(
                1,
                "John Doe",
                "1234567812345678",
                "12/26",
                "123",
                "Personal Card"
        );
        PaymentMethod method2 = new PaymentMethod(
                2,
                "Jane Smith",
                "8765432187654321",
                "11/25",
                "456",
                "Business Card"
        );
        paymentMethodRepo.save(method1);
        paymentMethodRepo.save(method2);

        // When
        Iterable<PaymentMethod> paymentMethods = paymentMethodRepo.findAll();

        // Then
        assertThat(paymentMethods).hasSize(2);
        assertThat(paymentMethods).extracting("nickname").contains("Personal Card", "Business Card");
    }
}
