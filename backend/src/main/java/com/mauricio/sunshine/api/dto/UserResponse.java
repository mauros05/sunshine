package com.mauricio.sunshine.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
  UUID id,
  String fullName,
  String email,
  boolean active,
  LocalDateTime createdAt
)
{}
