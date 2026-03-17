package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID orderId,
        PaymentMethod method,
        BigDecimal amount,
        LocalDateTime paidAt
) {
}
