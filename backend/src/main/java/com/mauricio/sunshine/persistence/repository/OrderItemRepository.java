package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.OrderItemEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

  @EntityGraph(attributePaths = "product")
  List<OrderItemEntity> findByOrderId(UUID orderId);
}
