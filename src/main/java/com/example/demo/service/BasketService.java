package com.example.demo.service;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.Product;
import com.example.demo.repository.BasketRepository;
import com.example.demo.exception.BasketException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BasketService {
    private static final String BASKET_NOT_FOUND = "Basket not found";
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    private final BasketRepository basketRepository;
    private final ProductService productService;

    public BasketService(BasketRepository basketRepository, ProductService productService) {
        this.basketRepository = basketRepository;
        this.productService = productService;
    }

    @Transactional
    public Basket createBasket() {
        Basket basket = new Basket();
        basket = basketRepository.save(basket);
        return basket;
    }

    @Transactional
    public Basket getOrCreateBasket() {
        // First try to find an existing basket
        Basket basket = basketRepository.findFirstByOrderByCreatedDateDesc();
        if (basket == null) {
            // If none exists, create a new one
            basket = createBasket();
        }
        return basket;
    }

    @Transactional
    public Basket addItemToBasket(Long basketId, Long productId, int quantity) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));

        // Check if item already exists in basket
        BasketItem existingItem = basket.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Update quantity if item already exists
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            // Create new item if it doesn't exist
            BasketItem item = new BasketItem();
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setBasket(basket);
            basket.addItem(item);
        }

        return basketRepository.save(basket);
    }

    @Transactional
    public Basket removeItemFromBasket(Long basketId, Long itemId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));

        basket.getItems().removeIf(item -> item.getId().equals(itemId));
        return basketRepository.save(basket);
    }

    @Transactional(readOnly = true)
    public Basket getBasket(Long basketId) {
        return basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));
    }

    @Transactional
    public Basket clearBasket(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));

        basket.getItems().clear();
        return basketRepository.save(basket);
    }
}
