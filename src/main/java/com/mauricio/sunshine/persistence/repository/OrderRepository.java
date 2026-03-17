package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> findByRestaurantId(UUID restaurantId);

    Optional<OrderEntity> findByIdAndRestaurantId(UUID orderId, UUID restaurantId);

    List<OrderEntity> findByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status);

}
