package com.mauricio.sunshine.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(
  name = "restaurant_memberships",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "restaurant_id"})
  }
)
public class RestaurantMembershipEntity {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private RestaurantEntity restaurant;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public RestaurantMembershipEntity() {
  }

  public RestaurantMembershipEntity(UserEntity user, RestaurantEntity restaurant, UserRole role) {
    this.user = user;
    this.restaurant = restaurant;
    this.role = role;
    this.createdAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public UserEntity getUser() {
    return user;
  }

  public RestaurantEntity getRestaurant() {
    return restaurant;
  }

  public UserRole getRole() {
    return role;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public void setRestaurant(RestaurantEntity restaurant) {
    this.restaurant = restaurant;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

}
