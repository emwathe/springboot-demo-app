package com.example.demo.controller;

import com.example.demo.entity.CreditCard;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/checkout")
    public ResponseEntity<String> processPayment(
            @RequestParam Long basketId,
            @RequestParam double totalAmount,
            @Valid @ModelAttribute CreditCard creditCard) {
        
        try {
            paymentService.processPayment(creditCard, totalAmount, basketId);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Payment failed: " + e.getMessage());
        }
    }
}
