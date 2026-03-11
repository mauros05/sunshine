package com.mauricio.sunshine.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RestaurantResponse(UUID id, String name, String address, LocalDateTime createdAt) {
}
