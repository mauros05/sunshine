package com.mauricio.sunshine.api.dto;

import com.mauricio.sunshine.persistence.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PayOrderRequest(
        @NotNull
        PaymentMethod method,

        @NotNull
        @DecimalMin(value = "0.01", message = "amount must be > 0")
        BigDecimal amount
) {
}
