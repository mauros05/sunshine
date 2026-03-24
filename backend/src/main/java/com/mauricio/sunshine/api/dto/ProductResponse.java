package com.mauricio.sunshine.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID restaurantId,
        String name,
        BigDecimal price,
        String category,
        boolean active,
        LocalDateTime createdAt
) {
}
