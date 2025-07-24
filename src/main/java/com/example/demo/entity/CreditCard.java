package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.*;

@Entity
@Table(name = "credit_cards")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Card number is required")
    @Pattern(regexp = "^(?:4\\d{12}(?:\\d{3})?|5[1-5]\\d{14}|6(?:011|5\\d{2})\\d{12}|3[47]\\d{13}|3(?:0[0-5]|[68]\\d)\\d{11}|(?:2131|1800|35\\d{3})\\d{11})$",
            message = "Invalid credit card number")
    private String cardNumber;

    @NotNull(message = "Card holder name is required")
    @Size(min = 2, max = 50, message = "Card holder name must be between 2 and 50 characters")
    private String cardHolderName;

    @NotNull(message = "Expiration date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$",
            message = "Invalid expiration date format (MM/YY)")
    private String expirationDate;

    @NotNull(message = "CVV is required")
    @Pattern(regexp = "^\\d{3}$",
            message = "Invalid CVV format (3 digits)")
    private String cvv;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
