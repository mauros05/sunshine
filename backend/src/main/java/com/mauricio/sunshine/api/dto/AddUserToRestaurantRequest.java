package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.UserRole;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddUserToRestaurantRequest(
  @NotNull
  UUID userId,

  @NotNull
  UserRole role
) {}
