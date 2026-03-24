package com.mauricio.sunshine.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank
        String name,

        @NotNull
        @DecimalMin(value = "0.01", inclusive = true, message = "El precio debe ser mayor a 0")
        @Digits(integer = 18, fraction = 2, message = "El precio debe tener hasta 2 decimales")
        BigDecimal price,

        @NotBlank
        String category
) {}
