package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.*;
import com.mauricio.sunshine.persistence.repository.OrderRepository;
import com.mauricio.sunshine.persistence.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public PaymentService(OrderRepository orderRepo, PaymentRepository paymentRepo) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public PaymentEntity payOrder(UUID restaurantId, UUID orderId, PaymentMethod method, BigDecimal amount) {
        OrderEntity order = orderRepo.findByIdAndRestaurantId(orderId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Order no encontrada"));

        if (order.getStatus() != OrderStatus.OPEN) {
            throw new IllegalArgumentException("Solo ordenes en OPEN pueden ser pagadas");
        }

        if (amount.compareTo(order.getTotal()) != 0) {
            throw new IllegalArgumentException("El importe del pago debe coincidir con el total del pedido.");
        }

        PaymentEntity payment = new PaymentEntity(order, method, amount);
        paymentRepo.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepo.save(order);

        return payment;
    }
}
