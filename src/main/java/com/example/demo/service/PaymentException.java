package com.example.demo.service;

import java.time.LocalDateTime;

public class PaymentException extends RuntimeException {
    private final LocalDateTime timestamp;
    private final String cardNumber;
    private final double amount;
    private final String basketId;

    public PaymentException(String message, String cardNumber, double amount, String basketId) {
        super(message);
        this.timestamp = LocalDateTime.now();
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.basketId = basketId;
    }

    public String getFormattedMessage() {
        return String.format("PAYMENT FAILURE %s | Card: %s | Amount: $%.2f | Basket: %s | Error: %s",
            timestamp, cardNumber, amount, basketId, getMessage());
    }
}
