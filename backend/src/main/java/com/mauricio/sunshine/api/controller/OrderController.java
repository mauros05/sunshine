package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.*;
import com.mauricio.sunshine.persistence.entity.OrderEntity;
import com.mauricio.sunshine.persistence.entity.OrderItemEntity;
import com.mauricio.sunshine.persistence.entity.OrderStatus;
import com.mauricio.sunshine.persistence.entity.PaymentEntity;
import com.mauricio.sunshine.service.OrderService;
import com.mauricio.sunshine.service.PaymentService;
import com.mauricio.sunshine.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/orders")
public class OrderController {
  private final OrderService orderService;
  private final PaymentService paymentService;
  private final PermissionService permissionService;

  public OrderController(OrderService orderService, PaymentService paymentService, PermissionService permissionService) {
    this.orderService = orderService;
    this.paymentService = paymentService;
    this.permissionService = permissionService;
  }

  @GetMapping
  public PageResponse<OrderResponse> getOrders(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @RequestParam(required = false) OrderStatus status,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

    var pageable = PageRequest.of(
      page,
      size,
      Sort.by(Sort.Direction.DESC, "createdAt")
    );

    Page<OrderEntity> ordersPage = orderService.getOrdersByRestaurantPaged(
      restaurantId,
      status,
      pageable
    );

    Page<OrderResponse> responsePage = ordersPage.map(order ->
      toResponse(order, orderService.getItems(order.getId()))
    );

    return new PageResponse<>(
      responsePage.getContent(),
      responsePage.getNumber(),
      responsePage.getSize(),
      responsePage.getTotalElements(),
      responsePage.getTotalPages(),
      responsePage.hasNext(),
      responsePage.hasPrevious()
    );
  }

  @PostMapping
  public OrderResponse createOrder(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @Valid @RequestBody(required = false) CreateOrderRequest req
  ) {

    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

    List<AddOrderItemRequest> items = req == null || req.items() == null ? List.of() : req.items();

    OrderEntity order = orderService.createOrder(restaurantId, items);
    return toResponse(order, orderService.getItems(order.getId()));
  }

  @PostMapping("/{orderId}/items")
  public OrderResponse addItem(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID orderId,
    @Valid @RequestBody AddOrderItemRequest req
  ) {

    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

    OrderEntity order = orderService.addItem(
      restaurantId,
      orderId,
      req.productId(),
      req.quantity()
    );

    return toResponse(order, orderService.getItems(order.getId()));
  }

  @GetMapping("/{orderId}")
  public OrderResponse getOrder(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID orderId
  ) {

    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

    OrderEntity order = orderService.getOrder(
      restaurantId,
      orderId
    );

    return toResponse(order, orderService.getItems(order.getId()));
  }

  @PostMapping("/{orderId}/pay")
  public PaymentResponse payOrder(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID orderId,
    @Valid @RequestBody PayOrderRequest req
  ) {

    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

    PaymentEntity payment = paymentService.payOrder(
      restaurantId,
      orderId,
      req.method(),
      req.amount()
    );

    return  toPaymentResponse(payment);
  }

  @PatchMapping("/{orderId}/cancel")
  public OrderResponse cancelOrder(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID orderId
  ) {

    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

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
