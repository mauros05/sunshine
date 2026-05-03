package com.mauricio.sunshine.service;

import com.mauricio.sunshine.api.dto.SalesReportResponse;
import com.mauricio.sunshine.api.dto.SalesSummaryResponse;
import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.OrderStatus;
import com.mauricio.sunshine.persistence.repository.OrderRepository;
import com.mauricio.sunshine.persistence.repository.RestaurantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SalesReportService {

  private final OrderRepository orderRepo;
  private final RestaurantRepository restaurantRepo;

  public SalesReportService(OrderRepository orderRepo, RestaurantRepository restaurantRepo) {
    this.orderRepo = orderRepo;
    this.restaurantRepo = restaurantRepo;
  }

  @Transactional(readOnly = true)
  public SalesReportResponse getSalesReport(UUID restaurantId) {
    if (!restaurantRepo.existsById(restaurantId)) {
        throw new IllegalArgumentException("Restaurante no encontrado");
    }

    var paidOrdersList = orderRepo.findByRestaurantIdAndStatus(restaurantId, OrderStatus.PAID);

    BigDecimal totalSales = paidOrdersList.stream()
            .map(OrderEntity::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    long paidOrders = orderRepo.countByRestaurantIdAndStatus(restaurantId, OrderStatus.PAID);

    return new SalesReportResponse(
            restaurantId,
            paidOrders,
            totalSales
    );
  }

  @Transactional(readOnly = true)
  public SalesSummaryResponse getSalesSummary(UUID restaurantId, LocalDate from, LocalDate to) {
    if (!restaurantRepo.existsById(restaurantId)) {
      throw new IllegalArgumentException("Restaurant not found");
    }

    LocalDateTime fromDateTime = from.atStartOfDay();
    LocalDateTime toDateTime = to.plusDays(1).atStartOfDay();

    var paidOrders = orderRepo.findByRestaurantIdAndStatusAndCreatedAtBetween(restaurantId, OrderStatus.PAID, fromDateTime, toDateTime);

    BigDecimal totalSales = paidOrders.stream()
            .map(order -> order.getTotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    long paidOrdersCount = paidOrders.size();

    BigDecimal averageTicket = paidOrdersCount == 0 ? BigDecimal.ZERO : totalSales.divide(BigDecimal.valueOf(paidOrdersCount), 2, RoundingMode.HALF_UP);

    return new SalesSummaryResponse(
      restaurantId,
      from,
      to,
      paidOrdersCount,
      totalSales,
      averageTicket
    );
  }


}
