package com.example.demo.repository;

import com.example.demo.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Query("SELECT b FROM Basket b ORDER BY b.createdDate DESC")
    List<Basket> findAllByOrderByCreatedDateDesc();
    
    default Basket findFirstByOrderByCreatedDateDesc() {
        List<Basket> baskets = findAllByOrderByCreatedDateDesc();
        return baskets.isEmpty() ? null : baskets.get(0);
    }
}
