package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.UserRole;

import java.util.UUID;

public record LoginMembershipResponse(
  UUID restaurantId,
  String restaurantName,
  UserRole role
) {
}
