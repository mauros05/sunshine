package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

    List<OrderItemEntity> findByOrderId(UUID orderId);
}
