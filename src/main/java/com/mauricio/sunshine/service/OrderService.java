package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.*;
import com.mauricio.sunshine.persistence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.math.BigDecimal;
import java.util.List;

@Service
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
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

        OrderEntity order = new OrderEntity(restaurant);
        return orderRepo.save(order);
    }

    @Transactional
    public OrderEntity addItem(UUID restaurantId, UUID orderId, UUID productId, Integer quantity) {
        OrderEntity order = orderRepo.findByIdAndRestaurantId(orderId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new IllegalArgumentException("Solo Ordenes en OPEN pueden ser modificadas");
        }

        ProductEntity product = productRepo.findByIdAndRestaurantId(productId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (!product.isActive()) {
            throw new IllegalArgumentException("El producto esta inactivo");
        }

        OrderItemEntity item = new OrderItemEntity(order, product, quantity, product.getPrice());
        orderItemRepo.save(item);

        recalculateTotal(order);

        return order;
    }

    @Transactional(readOnly = true)
    public OrderEntity getOrder(UUID restaurantId, UUID orderId) {
        return orderRepo.findByIdAndRestaurantId(orderId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<OrderItemEntity> getItems(UUID orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }

    private void recalculateTotal(OrderEntity order) {
        List<OrderItemEntity> items = orderItemRepo.findByOrderId(order.getId());

        BigDecimal total = items.stream()
                .map(OrderItemEntity::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        orderRepo.save(order);
    }


}
