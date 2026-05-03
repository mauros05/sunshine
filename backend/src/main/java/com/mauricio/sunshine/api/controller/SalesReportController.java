package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.SalesReportResponse;
import com.mauricio.sunshine.api.dto.SalesSummaryResponse;
import com.mauricio.sunshine.service.SalesReportService;
import com.mauricio.sunshine.service.PermissionService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/sales-report")
public class SalesReportController {

  private final SalesReportService salesReportService;
  private final PermissionService permissionService;

  public SalesReportController(SalesReportService salesReportService, PermissionService permissionService) {
    this.salesReportService = salesReportService;
    this.permissionService = permissionService;
  }

  @GetMapping
  public SalesReportResponse getSalesReport(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId
  ){
    permissionService.requireOwnerOrManager(userId, restaurantId);
    return salesReportService.getSalesReport(restaurantId);
  }


  @GetMapping("/summary")
  public SalesSummaryResponse getSalesSummary(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
  ){
    permissionService.requireOwnerOrManager(userId, restaurantId);
    return salesReportService.getSalesSummary(restaurantId, from, to);
  }
}
