package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "basket_id")
    private List<BasketItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void addItem(BasketItem item) {
        items.add(item);
    }

    public void removeItem(BasketItem item) {
        items.remove(item);
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(BasketItem::getTotalPrice)
                .sum();
    }
}
