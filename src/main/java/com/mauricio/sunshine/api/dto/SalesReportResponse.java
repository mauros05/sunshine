package com.mauricio.sunshine.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SalesReportResponse(
        UUID restaurantId,
        long paidOrders,
        BigDecimal totalSales
) {
}
