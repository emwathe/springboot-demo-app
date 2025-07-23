package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BasketItem> items = new ArrayList<>();

    public Basket() {
        // No need to initialize items again, it's already initialized at declaration
        this.createdDate = new Date();
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
