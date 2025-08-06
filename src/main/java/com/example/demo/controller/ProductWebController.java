package com.example.demo.controller;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.Product;
import com.example.demo.service.BasketService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProductService;
import com.example.demo.service.SalesLogger;
import com.example.demo.exception.BasketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductWebController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private BasketService basketService;
    
    @Autowired
    private SalesLogger salesLogger;
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String listProducts(@RequestParam(required = false) Long basketId, Model model) {
        model.addAttribute("products", productService.getAllProducts());
        if (basketId == null) {
            Basket basket = basketService.getOrCreateBasket();
            basketId = basket.getId();
        }
        model.addAttribute("basketId", basketId);
        return "products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping
    public String addProduct(@ModelAttribute Product product, @RequestParam(required = false) Long basketId) {
        productService.saveProduct(product);
        if (basketId != null) {
            return "redirect:/products?basketId=" + basketId;
        } else {
            return "redirect:/products";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, @RequestParam(required = false) Long basketId) {
        model.addAttribute("product", productService.getProductById(id).orElse(new Product()));
        model.addAttribute("basketId", basketId);
        return "product_form";
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product, @RequestParam(required = false) Long basketId) {
        product.setId(id);
        productService.saveProduct(product);
        if (basketId != null) {
            return "redirect:/products?basketId=" + basketId;
        } else {
            return "redirect:/products";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @RequestParam(required = false) Long basketId) {
        productService.deleteProduct(id);
        if (basketId != null) {
            return "redirect:/products?basketId=" + basketId;
        } else {
            return "redirect:/products";
        }
    }

    @GetMapping("/add-to-basket/{productId}")
    public String addToBasket(@PathVariable Long productId, @RequestParam int quantity, @RequestParam(required = false) Long basketId, Model model) {
        if (basketId == null) {
            basketId = basketService.getOrCreateBasket().getId();
        }
        basketService.addItemToBasket(basketId, productId, quantity);
        model.addAttribute("basketId", basketId);
        model.addAttribute("successMessage", "Product added to basket");
        return "redirect:/products?basketId=" + basketId;
    }

    @GetMapping("/remove-from-basket/{itemId}")
    public String removeFromBasket(@PathVariable Long itemId, @RequestParam Long basketId, Model model) {
        basketService.removeItemFromBasket(basketId, itemId);
        model.addAttribute("successMessage", "Product removed from basket");
        return "redirect:/products/basket/" + basketId;
    }

    @GetMapping("/basket/{id}")
    public String viewBasket(@PathVariable Long id, Model model) {
        try {
            Basket basket = basketService.getBasket(id);
            if (basket != null) {
                model.addAttribute("basket", basket);
                return "basket";
            } else {
                model.addAttribute("errorMessage", "Basket not found");
                return "redirect:/products";
            }
        } catch (BasketException e) {
            model.addAttribute("errorMessage", "Basket not found");
            return "redirect:/products";
        }
    }

    @GetMapping("/clear-basket/{id}")
    public String clearBasket(@PathVariable Long id, Model model, @RequestParam(required = false) Long basketId) {
        basketService.clearBasket(id);
        model.addAttribute("successMessage", "Basket cleared successfully!");
        if (basketId != null) {
            return "redirect:/products?basketId=" + basketId;
        } else {
            return "redirect:/products";
        }
    }

    @GetMapping("/checkout/{id}")
    public String checkoutBasket(@PathVariable Long id, Model model) {
        try {
            Basket basket = basketService.getBasket(id);
            double total = basket.getTotalPrice();
            if (basket.getItems().isEmpty()) {
                model.addAttribute("errorMessage", "Basket is empty");
                return "redirect:/products";
            }
            model.addAttribute("basketId", basket.getId());
            model.addAttribute("totalAmount", total);
            return "payment";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Basket not found");
            return "redirect:/products";
        }
    }

    @PostMapping("/api/payment/checkout/{basketId}")
    public String processPayment(@PathVariable Long basketId,
                                @RequestParam double totalAmount,
                                @RequestParam String cardNumber,
                                @RequestParam String cardHolderName,
                                @RequestParam String expirationDate,
                                @RequestParam String cvv,
                                Model model) {
        try {
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber(cardNumber);
            creditCard.setCardHolderName(cardHolderName);
            creditCard.setExpirationDate(expirationDate);
            creditCard.setCvv(cvv);

            paymentService.processPayment(creditCard, totalAmount, basketId);
            model.addAttribute("successMessage", "Payment successful! Total: $" + totalAmount);
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Payment failed: " + e.getMessage());
            return "redirect:/products/basket/" + basketId;
        }
    }
}
