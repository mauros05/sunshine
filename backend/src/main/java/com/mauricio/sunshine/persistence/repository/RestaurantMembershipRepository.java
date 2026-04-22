package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantMembershipRepository extends JpaRepository<RestaurantMembershipEntity, UUID> {

  @EntityGraph(attributePaths = {"user", "restaurant"})
  List<RestaurantMembershipEntity> findByRestaurantId(UUID restaurantId);

  @EntityGraph(attributePaths = {"restaurant"})
  List<RestaurantMembershipEntity> findByUserId(UUID userId);

  Optional<RestaurantMembershipEntity> findByUserIdAndRestaurantId(UUID userId, UUID restaurantId);

  @EntityGraph(attributePaths = {"user", "restaurant"})
  Optional<RestaurantMembershipEntity> findByIdAndRestaurantId(UUID membershipId, UUID restaurantId);

  boolean existsByUserIdAndRestaurantId(UUID userId, UUID restaurantId);
}
