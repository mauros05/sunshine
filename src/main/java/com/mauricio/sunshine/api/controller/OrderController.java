package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.*;
import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.OrderItemEntity;
import com.mauricio.sunshine.persistence.entity.OrderStatus;
import com.mauricio.sunshine.persistence.entity.PaymentEntity;
import com.mauricio.sunshine.service.OrderService;
import com.mauricio.sunshine.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/orders")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<OrderResponse> getOrders(@PathVariable UUID restaurantId,
                                         @RequestParam(required = false) OrderStatus status) {
        return orderService.getOrdersByRestaurant(restaurantId, status)
                .stream()
                .map(order -> toResponse(order, orderService.getItems(order.getId())))
                .toList();
    }

    @PostMapping
    public OrderResponse createOrder(@PathVariable UUID restaurantId,
                                     @Valid @RequestBody(required = false) CreateOrderRequest req) {
        List<AddOrderItemRequest> items = req == null || req.items() == null ? List.of() : req.items();

        OrderEntity order = orderService.createOrder(restaurantId, items);
        return toResponse(order, orderService.getItems(order.getId()));
    }

    @PostMapping("/{orderId}/items")
    public OrderResponse addItem(@PathVariable UUID restaurantId,
                                 @PathVariable UUID orderId,
                                 @Valid @RequestBody AddOrderItemRequest req) {
        OrderEntity order = orderService.addItem(
                restaurantId,
                orderId,
                req.productId(),
                req.quantity()
        );

        return toResponse(order, orderService.getItems(order.getId()));
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable UUID restaurantId,
                                  @PathVariable UUID orderId
                                  ) {
        OrderEntity order = orderService.getOrder(restaurantId, orderId);
        return toResponse(order, orderService.getItems(order.getId()));
    }

    @PostMapping("/{orderId}/pay")
    public PaymentResponse payOrder(@PathVariable UUID restaurantId,
                                    @PathVariable UUID orderId,
                                    @Valid @RequestBody PayOrderRequest req) {
        PaymentEntity payment = paymentService.payOrder(
                restaurantId,
                orderId,
                req.method(),
                req.amount()
        );
        return  toPaymentResponse(payment);
    }

    @PatchMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(@PathVariable UUID restaurantId,
                                     @PathVariable UUID orderId) {
        OrderEntity order = orderService.cancelOrder(restaurantId, orderId);

        return toResponse(order, orderService.getItems(order.getId()));
    }

    private OrderResponse toResponse(OrderEntity order, List<OrderItemEntity> items) {
        return new OrderResponse(
                order.getId(),
                order.getRestaurant().getId(),
                order.getStatus(),
                order.getTotal(),
                order.getCreatedAt(),
                items.stream().map(this::toItemResponse).toList()
        );
    }

    private OrderItemResponse toItemResponse(OrderItemEntity item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }

    private PaymentResponse toPaymentResponse(PaymentEntity payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getMethod(),
                payment.getAmount(),
                payment.getPaidAt()
        );
    }

}
