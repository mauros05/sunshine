package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TicketResponse(
  UUID orderId,
  Long folio,
  String restaurantName,
  String restaurantAddress,
  LocalDateTime createdAt,
  LocalDateTime paidAt,
  PaymentMethod paymentMethod,
  BigDecimal total,
  List<TicketItemResponse> items
) {
}
