package com.example.demo.controller;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.Product;
import com.example.demo.service.BasketService;
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

    @GetMapping
    public String listProducts(@RequestParam(required = false) Long basketId, Model model) {
        model.addAttribute("products", productService.getAllProducts());
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
            basketId = basketService.createBasket().getId();
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
        return "redirect:/products?basketId=" + basketId;
    }

    @GetMapping({"/basket/{id}", "/products/basket/{id}"})
    public String viewBasket(@PathVariable Long id, Model model) {
        try {
            Basket basket = basketService.getBasket(id);
            model.addAttribute("basket", basket);
            return "basket";
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
            for (BasketItem item : basket.getItems()) {
                salesLogger.logSale(item.getProduct().getId(), item.getProduct().getName(), item.getTotalPrice());
            }
            model.addAttribute("successMessage", "Basket checked out successfully! Total: $" + total);
            basketService.clearBasket(id);
            return "redirect:/products";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Basket not found or empty");
            return "redirect:/products";
        }
    }
}
