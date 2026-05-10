package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.api.dto.TopProductResponse;
import com.mauricio.sunshine.persistence.entity.OrderItemEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

  @EntityGraph(attributePaths = "product")
  List<OrderItemEntity> findByOrderId(UUID orderId);

  @Query("""
      select new com.mauricio.sunshine.api.dto.TopProductResponse(
          i.product.id,
          i.product.name,
          sum(i.quantity),
          sum(i.subtotal)
      )
      from OrderItemEntity i
      where i.order.restaurant.id = :restaurantId
      and i.order.status = com.mauricio.sunshine.persistence.entity.OrderStatus.PAID
      and i.order.createdAt >= :from
      and i.order.createdAt < :to
      group by i.product.id, i.product.name
      order by sum(i.quantity) desc
      """)
  List<TopProductResponse>findTopProducts(
    UUID restaurantId,
    LocalDateTime from,
    LocalDateTime to
  );

}
