package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
  List<PaymentEntity> findByOrderId(UUID orderId);

  Optional<PaymentEntity> findFirstByOrderId(UUID orderId);
}
