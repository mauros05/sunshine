package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMembershipRoleRequest(
  @NotNull
  UserRole role
) {
}
