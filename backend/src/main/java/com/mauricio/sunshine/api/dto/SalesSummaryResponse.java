package com.mauricio.sunshine.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SalesSummaryResponse(
  UUID restaurantId,
  LocalDate from,
  LocalDate to,
  long paidOrders,
  BigDecimal totalSales,
  BigDecimal avarageTicket
) {
}
