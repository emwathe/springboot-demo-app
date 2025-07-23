package com.example.demo.service;

import com.example.demo.entity.CreditCard;
import com.example.demo.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import jakarta.validation.annotation.Validated;

@Service
@Validated
public class PaymentService {
    @Autowired
    private CreditCardRepository creditCardRepository;

    public boolean validateCreditCard(@Valid CreditCard creditCard) {
        // Additional validation for card number using Luhn algorithm
        if (!isValidCreditCardNumber(creditCard.getCardNumber())) {
            throw new IllegalArgumentException("Invalid credit card number format");
        }

        // Check if card exists in database
        CreditCard existingCard = creditCardRepository.findByCardNumber(creditCard.getCardNumber());
        if (existingCard != null) {
            throw new IllegalArgumentException("This credit card has already been used");
        }

        return true;
    }

    private boolean isValidCreditCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public void processPayment(CreditCard creditCard, double amount) {
        // Simulate payment processing
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // Save the credit card information
        creditCardRepository.save(creditCard);
    }
}
