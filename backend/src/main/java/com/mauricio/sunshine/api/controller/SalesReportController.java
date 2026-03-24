package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.SalesReportResponse;
import com.mauricio.sunshine.service.SalesReportService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/sales-report")
public class SalesReportController {

    private final SalesReportService salesReportService;

    public SalesReportController(SalesReportService salesReportService) {
        this.salesReportService = salesReportService;
    }

    @GetMapping
    public SalesReportResponse getSalesReport(@PathVariable UUID restaurantId){
        return salesReportService.getSalesReport(restaurantId);
    }
}
