package com.mauricio.sunshine.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String fullName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private boolean active;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  public UserEntity(){
  }

  public UserEntity(String fullName, String email) {
    this.fullName = fullName;
    this.email = email;
    this.active = true;
    this.createdAt = LocalDateTime.now();
  }

  public UUID getId() {
    return id;
  }

  public String getFullName() {
    return fullName;
  }

  public String getEmail() {
    return email;
  }

  public boolean isActive() {
    return active;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
