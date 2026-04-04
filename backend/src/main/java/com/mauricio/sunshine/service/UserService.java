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

}
