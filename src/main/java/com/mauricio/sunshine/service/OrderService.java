package com.mauricio.sunshine.service;

import com.mauricio.sunshine.api.dto.AddOrderItemRequest;
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
    public OrderEntity createOrder(UUID restaurantId, List<AddOrderItemRequest> items) {
        RestaurantEntity restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

        OrderEntity order = new OrderEntity(restaurant);
        OrderEntity savedOrder = orderRepo.save(order);

        for (AddOrderItemRequest item : items) {
            addItemToOrder(savedOrder, restaurantId, item.productId(), item.quantity());
        }

        recalculateTotal(savedOrder);

        return savedOrder;
    }

    @Transactional
    public OrderEntity addItem(UUID restaurantId, UUID orderId, UUID productId, Integer quantity) {
        OrderEntity order = orderRepo.findByIdAndRestaurantId(orderId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new IllegalArgumentException("Solo Ordenes en OPEN pueden ser modificadas");
        }

        addItemToOrder(order, restaurantId, productId, quantity);

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

    @Transactional(readOnly = true)
    public List<OrderEntity> getOrdersByRestaurant(UUID restaurantId, OrderStatus status){
        if (!restaurantRepo.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurante no encontrado");
        }

        if (status == null) {
            return orderRepo.findByRestaurantId(restaurantId);
        }

        return orderRepo.findByRestaurantIdAndStatus(restaurantId, status);
    }

    private void addItemToOrder(OrderEntity order, UUID restaurantId, UUID productId, Integer quantity) {
        ProductEntity product = productRepo.findByIdAndRestaurantId(productId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (!product.isActive()) {
            throw new IllegalArgumentException("El producto esta inactivo");
        }

        OrderItemEntity item = new OrderItemEntity(order, product, quantity, product.getPrice());
        orderItemRepo.save(item);
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
