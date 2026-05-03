package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

  List<OrderEntity> findByRestaurantId(UUID restaurantId);

  List<OrderEntity> findByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status);

  List<OrderEntity> findByRestaurantIdAndStatusAndCreatedAtBetween(UUID restaurantId, OrderStatus status, java.time.LocalDateTime from, java.time.LocalDateTime to);

  Page<OrderEntity> findByRestaurantId(UUID restaurantId, Pageable pageable);

  Page<OrderEntity> findByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status, Pageable pageable);

  Optional<OrderEntity> findByIdAndRestaurantId(UUID orderId, UUID restaurantId);

  long countByRestaurantIdAndStatus(UUID restaurantId, OrderStatus status);
}
