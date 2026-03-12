package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.*;
import com.mauricio.sunshine.persistence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.List;

public class OrderService {

    private final RestaurantRepository restaurantRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderService(RestaurantRepository restaurantRepo,
                        ProductRepository productRepo,
                        OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo
                        ) {
        this.restaurantRepo = restaurantRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Transactional
    public OrderEntity createOrder(UUID restaurantId) {
        RestaurantEntity restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        OrderEntity order = new OrderEntity(restaurant);
        return orderRepo.save(order);
    }

    @


}
