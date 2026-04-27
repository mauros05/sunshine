package com.mauricio.sunshine.api.dto;

import java.util.List;

public record PageResponse<T>(
  List<T> items,
  int page,
  int size,
  long totalItems,
  int totalPages,
  boolean hasNext,
  boolean hasPrevious) {
}
