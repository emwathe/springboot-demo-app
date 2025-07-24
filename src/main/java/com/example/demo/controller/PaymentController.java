package com.example.demo.controller;

import com.example.demo.entity.CreditCard;
import com.example.demo.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/api/payment")
public class PaymentController {
    private static final String REDIRECT_PRODUCTS = "redirect:/products";
    
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/checkout")
    public String processPayment(
            @RequestParam Long basketId,
            @RequestParam double totalAmount,
            @Valid @ModelAttribute CreditCard creditCard,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:");
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append("\n").append(error.getDefaultMessage());
            });
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage.toString());
            return REDIRECT_PRODUCTS;
        }
        
        try {
            paymentService.processPayment(creditCard, totalAmount, basketId);
            redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully! Your basket has been cleared.");
            return REDIRECT_PRODUCTS;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment failed: " + e.getMessage());
            return REDIRECT_PRODUCTS;
        }
    }
}
