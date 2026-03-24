package com.mauricio.sunshine.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateRestaurantRequest(@NotBlank String name, @NotBlank String address) {}
