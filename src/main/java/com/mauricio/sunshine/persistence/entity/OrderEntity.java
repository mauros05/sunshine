package com.mauricio.sunshine.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public OrderEntity(){
    }

    public OrderEntity(RestaurantEntity restaurant){
        this.restaurant = restaurant;
        this.status = OrderStatus.OPEN;
        this.total = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setRestaurant(RestaurantEntity restaurant){
        this.restaurant = restaurant;
    }

    public void setStatus(OrderStatus status){
        this.status = status;
    }

    public void setTotal(BigDecimal total){
        this.total = total;
    }

}
