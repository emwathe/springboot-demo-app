package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SalesLogger {
    private static final Logger logger = LoggerFactory.getLogger(SalesLogger.class);

    public void logSale(Long productId, String productName, Double price) {
        logger.info("Product sold - ID: {}, Name: {}, Price: ${}", productId, productName, price);
    }
}
