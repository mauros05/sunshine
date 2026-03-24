package com.mauricio.sunshine.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    public PaymentEntity(){
    }

    public PaymentEntity(OrderEntity order, PaymentMethod method, BigDecimal amount){
        this.order = order;
        this.method = method;
        this.amount = amount;
        this.paidAt = LocalDateTime.now();
    }

    public UUID getId(){
        return id;
    }

    public OrderEntity getOrder(){
        return order;
    }

    public PaymentMethod getMethod(){
        return method;
    }

    public BigDecimal getAmount(){
        return amount;
    }

    public LocalDateTime getPaidAt(){
        return paidAt;
    }

    public void setOrder(OrderEntity order){
        this.order = order;
    }

    public void setMethod(PaymentMethod method){
        this.method = method;
    }

    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public void setPaidAt(LocalDateTime paidAt){
        this.paidAt = paidAt;
    }
}
