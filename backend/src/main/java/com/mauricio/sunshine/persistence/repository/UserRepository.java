package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  Optional<UserEntity> findByEmail(String email);
  boolean existsByEmail(String email);
}
