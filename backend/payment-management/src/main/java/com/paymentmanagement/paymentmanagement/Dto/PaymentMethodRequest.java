package com.paymentmanagement.paymentmanagement.Dto;
import lombok.Data;

@Data
public class PaymentMethodRequest {
    private int userId;
    private String cardHolderName;
    private String cardNumber;
    private String expirationDate;
    private String cvv;
    private String nickname;


    public PaymentMethodRequest(int userId, String cardHolderName, String cardNumber, String expirationDate, String cvv, String nickname) {
    }
}
