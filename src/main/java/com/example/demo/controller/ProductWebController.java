package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductWebController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form";
    }

    @PostMapping
    public String addProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        productService.getProductById(id).ifPresent(p -> model.addAttribute("product", p));
        return "product_form";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
