package com.mauricio.sunshine.api.dto;

import jakarta.validation.Valid;

import java.util.List;

public record CreateOrderRequest(
        @Valid
        List<AddOrderItemRequest> items
) {}
