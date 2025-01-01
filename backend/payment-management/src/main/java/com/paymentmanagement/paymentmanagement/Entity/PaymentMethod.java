package com.paymentmanagement.paymentmanagement.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment_details")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private String cardNumber; // encrypt this in production

    @Column(nullable = false)
    private String expirationDate;

    @Column(nullable = false)
    private String cvv; //  hash this in production

    @Column(nullable = false)
    private String nickname;

    public PaymentMethod(int i, String creditCard) {

    }

    public PaymentMethod(int userId, String cardHolderName, String cardNumber, String expirationDate, String cvv, String nickname) {
        this.userId = userId;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.nickname = nickname;
    }

    public PaymentMethod() {

    }

    public boolean getMethodName() {

        return false;
    }
}
