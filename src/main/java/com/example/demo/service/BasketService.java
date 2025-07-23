package com.example.demo.service;

import com.example.demo.entity.Basket;
import com.example.demo.entity.BasketItem;
import com.example.demo.entity.Product;
import com.example.demo.repository.BasketRepository;
import com.example.demo.exception.BasketException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BasketService {
    private static final String BASKET_NOT_FOUND = "Basket not found";
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public Basket createBasket() {
        Basket basket = new Basket();
        basket = basketRepository.save(basket);
        return basket;
    }

    @Transactional
    public Basket addItemToBasket(Long basketId, Long productId, int quantity) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));

        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException(PRODUCT_NOT_FOUND));

        BasketItem item = new BasketItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setBasket(basket); // Set the basket reference
        basket.addItem(item);

        return basketRepository.save(basket);
    }

    @Transactional
    public void removeItemFromBasket(Long basketId, Long itemId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));

        basket.getItems().removeIf(item -> item.getId().equals(itemId));
        basketRepository.save(basket);
    }

    @Transactional(readOnly = true)
    public Basket getBasket(Long basketId) {
        return basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));
    }

    @Transactional
    public void clearBasket(Long basketId) {
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new BasketException(BASKET_NOT_FOUND));

        basket.getItems().clear();
        basketRepository.save(basket);
    }
}
