package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketItem> items = new ArrayList<>();

    public Basket() {
        items = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void addItem(BasketItem item) {
        items.add(item);
        item.setBasket(this); // Set the bidirectional relationship
    }

    public void removeItem(BasketItem item) {
        items.remove(item);
        item.setBasket(null); // Remove the relationship
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(BasketItem::getTotalPrice)
                .sum();
    }
}
