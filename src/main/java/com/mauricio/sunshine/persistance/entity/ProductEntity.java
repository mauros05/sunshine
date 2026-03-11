package com.mauricio.sunshine.persistance.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public  ProductEntity(RestaurantEntity restaurant, String name, BigDecimal price, String category) {
        this.restaurant = restaurant;
        this.name = name;
        this.price = price;
        this.category = category;
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    
}
