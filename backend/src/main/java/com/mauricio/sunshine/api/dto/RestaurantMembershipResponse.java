package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record RestaurantMembershipResponse(
  UUID id,
  UUID userId,
  String userName,
  String userEmail,
  UUID restaurantId,
  String restaurantName,
  UserRole role,
  LocalDateTime createdAt
) {}
