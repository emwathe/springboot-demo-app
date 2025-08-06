package com.example.demo.service;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.CreditCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SalesLogger {
    private static final Logger logger = LoggerFactory.getLogger(SalesLogger.class);

    public void logCheckout(String cardNumber, double totalAmount, List<BasketItem> items) {
        String cardType = determineCardType(cardNumber);
        String products = formatProducts(items);
        logger.info("SALE {} | Card: {} | Amount: ${} | Products: {}",
            getCurrentTimestamp(), cardType, totalAmount, products);
    }

    private String getCurrentTimestamp() {
        return java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private String determineCardType(String cardNumber) {
        // Simple card type detection based on first digits
        if (cardNumber.startsWith("4")) return "Visa";
        if (cardNumber.startsWith("5")) return "MasterCard";
        if (cardNumber.startsWith("3")) return "American Express";
        return "Unknown";
    }

    private String formatProducts(List<BasketItem> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            BasketItem item = items.get(i);
            sb.append(String.format("%s (ID: %d, $%.2f x%d)",
                item.getProduct().getName(),
                item.getProduct().getId(),
                item.getTotalPrice(),
                item.getQuantity()));
            if (i < items.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
