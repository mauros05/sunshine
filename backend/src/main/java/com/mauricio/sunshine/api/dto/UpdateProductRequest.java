package com.mauricio.sunshine.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateProductRequest(
  @NotBlank
  String name,

  @NotNull
  @DecimalMin(value = "0.01", message = "price must be > 0")
  BigDecimal price,

  @NotNull
  String category
) {
}
