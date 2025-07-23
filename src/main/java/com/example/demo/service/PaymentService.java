package com.example.demo.service;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.CreditCard;
import com.example.demo.repository.BasketRepository;
import com.example.demo.repository.CreditCardRepository;
import com.example.demo.service.BasketService;
import com.example.demo.service.SalesLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validated
public class PaymentService {
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

    @Transactional
    public void processPayment(@jakarta.validation.Valid CreditCard creditCard, double amount, Long basketId) {
        // Validate payment amount
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        // Validate credit card
        validateCreditCard(creditCard);

        // Process payment
        creditCardRepository.save(creditCard);

        // Log sales
        Basket basket = basketService.getBasket(basketId);
        for (BasketItem item : basket.getItems()) {
            salesLogger.logSale(item.getProduct().getId(), item.getProduct().getName(), item.getTotalPrice());
        }

        // Clear basket
        basketService.clearBasket(basketId);
    }
}
