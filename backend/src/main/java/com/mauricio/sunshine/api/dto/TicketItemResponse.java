package com.mauricio.sunshine.api.dto;

import java.math.BigDecimal;

public record TicketItemResponse(
  String productName,
  Integer quantity,
  BigDecimal unitPrice,
  BigDecimal subtotal
) {
}
