package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.*;
import com.mauricio.sunshine.persistence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

  private final UserRepository userRepo;
  private final RestaurantRepository restaurantRepo;
  private final RestaurantMembershipRepository membershipRepo;

  public UserService(UserRepository userRepo,
                     RestaurantRepository restaurantRepo,
                     RestaurantMembershipRepository membershipRepo
  ) {
    this.userRepo = userRepo;
    this.restaurantRepo = restaurantRepo;
    this.membershipRepo = membershipRepo;
  }

  @Transactional
  public UserEntity createUser(String fullName, String email) {
    if (userRepo.existsByEmail(email)) {
      throw new IllegalArgumentException("Email already exists");
    }

    UserEntity user = new UserEntity(fullName, email);
    return userRepo.save(user);
  }

  @Transactional(readOnly = true)
  public List<UserEntity> getUsers() {
    return userRepo.findAll();
  }

  @Transactional
  public RestaurantMembershipEntity addUserToRestaurant(UUID restaurantId, UUID userId, UserRole role) {
    RestaurantEntity restaurant = restaurantRepo.findById(restaurantId)
    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

    UserEntity user = userRepo.findById(userId)
    .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (membershipRepo.existsByUserIdAndRestaurantId(userId, restaurantId)) {
      throw new IllegalArgumentException("User already belongs to this restaurant");
    }

    RestaurantMembershipEntity membership = new RestaurantMembershipEntity(user, restaurant, role);
    return membershipRepo.save(membership);
  }

  @Transactional(readOnly = true)
  public List<RestaurantMembershipEntity> getMembershipsByRestaurant(UUID restaurantId) {
    if (!restaurantRepo.existsById(restaurantId)) {
      throw new IllegalArgumentException("Restaurant not found");
    }

    return membershipRepo.findByRestaurantId(restaurantId);
  }

  @Transactional
  public RestaurantMembershipEntity updateMembershipRole(UUID restaurantId, UUID membershipId, UserRole role){
    RestaurantMembershipEntity membership = membershipRepo.findByIdAndRestaurantId(membershipId, restaurantId)
    .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

    membership.setRole(role);
    return membershipRepo.save(membership);
  }

  @Transactional
  public void removeMembership(UUID restaurantId, UUID membershipId) {
    RestaurantMembershipEntity membership = membershipRepo.findByIdAndRestaurantId(membershipId, restaurantId)
    .orElseThrow(() -> new IllegalArgumentException("Membership not found"));

    membershipRepo.delete(membership);
  }



}
