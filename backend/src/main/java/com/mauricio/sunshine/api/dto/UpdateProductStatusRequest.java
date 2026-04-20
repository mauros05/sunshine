package com.mauricio.sunshine.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateProductStatusRequest(@NotNull Boolean active) {
}
