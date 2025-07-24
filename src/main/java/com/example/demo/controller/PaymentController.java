package com.example.demo.controller;

import com.example.demo.entity.CreditCard;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/checkout", consumes = {"application/json", "application/x-www-form-urlencoded"})
    public ResponseEntity<String> processPayment(
            @RequestParam Long basketId,
            @RequestParam double totalAmount,
            @Valid @ModelAttribute CreditCard creditCard,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:");
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append("\n").append(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }
        
        try {
            paymentService.processPayment(creditCard, totalAmount, basketId);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Payment failed: " + e.getMessage());
        }
    }
}
