package com.mauricio.sunshine.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicketResponse(
  UUID orderId,
  String restaurantName,
  LocalDateTime createdAt,
  BigDecimal total,
  List<TicketItemResponse> items
) {
}
