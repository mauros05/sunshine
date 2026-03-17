package com.mauricio.sunshine.service;

import com.mauricio.sunshine.api.dto.SalesReportResponse;
import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.OrderStatus;
import com.mauricio.sunshine.persistence.repository.OrderRepository;
import com.mauricio.sunshine.persistence.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
}
