package com.mauricio.sunshine.persistance.repository;

import com.mauricio.sunshine.persistance.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, UUID> {
}
