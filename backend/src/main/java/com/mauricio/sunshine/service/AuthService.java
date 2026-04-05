package com.mauricio.sunshine.service;

import com.mauricio.sunshine.api.dto.LoginMembershipResponse;
import com.mauricio.sunshine.api.dto.LoginResponse;
import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import com.mauricio.sunshine.persistence.entity.UserEntity;
import com.mauricio.sunshine.persistence.repository.RestaurantMembershipRepository;
import com.mauricio.sunshine.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService {

  private final UserRepository userRepo;
  private final RestaurantMembershipRepository membershipRepo;

  public AuthService(UserRepository userRepo, RestaurantMembershipRepository membershipRepo) {
    this.userRepo = userRepo;
    this.membershipRepo = membershipRepo;
  }

  @Transactional(readOnly = true)
  public LoginResponse loginByEmail(String email) {
    UserEntity user = userRepo.findByEmail(email)
    .orElseThrow(() -> new IllegalArgumentException("User not found"));

    List<RestaurantMembershipEntity> memberships = membershipRepo.findByUserId(user.getId());

    List<LoginMembershipResponse> membershipResponses = memberships.stream()
    .map(membership -> new LoginMembershipResponse(
      membership.getRestaurant().getId(),
      membership.getRestaurant().getName(),
      membership.getRole()
    )).toList();

    return new LoginResponse(
      user.getId(),
      user.getFullName(),
      user.getEmail(),
      membershipResponses
    );
  }
}
