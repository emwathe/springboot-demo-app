package com.example.demo.service;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.CreditCard;
import com.example.demo.repository.CreditCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
    @Autowired
    private CreditCardRepository creditCardRepository;
    
    @Autowired
    private BasketService basketService;
    
    @Autowired
    private SalesLogger salesLogger;
    
    public boolean validateCreditCard(@jakarta.validation.Valid CreditCard creditCard) {
        // Additional validation for card number using Luhn algorithm
        if (!isValidCreditCardNumber(creditCard.getCardNumber())) {
            throw new IllegalArgumentException("Invalid credit card number format");
        }

        // Test card number that should always be declined
        if (creditCard.getCardNumber().equals("5454545454545454")) {
            throw new PaymentException("Payment declined: Invalid card number", 
                maskCardNumber(creditCard.getCardNumber()), 
                0.0, // Amount not relevant for this error
                "N/A"); // Basket ID not relevant for this error
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

    @Transactional
    public void processPayment(@jakarta.validation.Valid CreditCard creditCard, double amount, Long basketId) {
        try {
            // Validate payment amount
            if (amount <= 0) {
                throw new PaymentException("Payment amount must be greater than 0", 
                    maskCardNumber(creditCard.getCardNumber()), amount, basketId.toString());
            }

            // Validate credit card
            validateCreditCard(creditCard);

            // Process payment
            creditCardRepository.save(creditCard);

            // Get basket and log checkout
            Basket basket = basketService.getBasket(basketId);
            salesLogger.logCheckout(maskCardNumber(creditCard.getCardNumber()), amount, basket.getItems());

            // Clear basket - this will now delete the basket
            basketService.clearBasket(basketId);
        } catch (PaymentException e) {
            logger.error(e.getFormattedMessage());
            throw e;
        } catch (Exception e) {
            logger.error("PAYMENT FAILURE {} | Card: {} | Amount: ${} | Basket: {} | Error: {}",
                java.time.LocalDateTime.now(),
                maskCardNumber(creditCard.getCardNumber()),
                amount,
                basketId,
                e.getMessage());
            throw new RuntimeException("Payment failed: " + e.getMessage(), e);
        }
    }
}
