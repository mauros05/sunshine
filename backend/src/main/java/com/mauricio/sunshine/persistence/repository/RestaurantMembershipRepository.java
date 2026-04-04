package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantMembershipRepository extends JpaRepository<RestaurantMembershipEntity, UUID> {

  List<RestaurantMembershipEntity> findByRestaurantId(UUID restaurantId);

  List<RestaurantMembershipEntity> findByUserId(UUID userId);

  Optional<RestaurantMembershipEntity> findByUserIdAndRestaurantId(UUID userId, UUID restaurantId);

  boolean existsByUserIdAndRestaurantId(UUID userId, UUID restaurantId);
}
