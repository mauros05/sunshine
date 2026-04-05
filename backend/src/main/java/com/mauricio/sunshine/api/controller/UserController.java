package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.*;
import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import com.mauricio.sunshine.persistence.entity.UserEntity;
import com.mauricio.sunshine.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public UserResponse createUser(@Valid @RequestBody CreateUserRequest req) {
    UserEntity user = userService.createUser(req.fullName(), req.email());
    return toUserResponse(user);
  }

  @GetMapping("/users")
  public List<UserResponse> getUsers() {
    return userService.getUsers()
      .stream()
      .map(this::toUserResponse)
      .toList();
  }

  @PostMapping("/restaurants/{restaurantId}/members")
  public RestaurantMembershipResponse addUserToRestaurant(@PathVariable UUID restaurantId, @Valid @RequestBody AddUserToRestaurantRequest req) {
    RestaurantMembershipEntity membership = userService.addUserToRestaurant(
      restaurantId,
      req.userId(),
      req.role()
    );

    return toMembershipResponse(membership);
  }

  @GetMapping("/restaurants/{restaurantId}/members")
  public List<RestaurantMembershipResponse> getMembersByRestautant(@PathVariable UUID restaurantId) {
    return userService.getMembershipsByRestaurant(restaurantId)
    .stream()
    .map(this::toMembershipResponse)
    .toList();
  }

  private UserResponse toUserResponse(UserEntity user) {
    return new UserResponse(
      user.getId(),
      user.getFullName(),
      user.getEmail(),
      user.isActive(),
      user.getCreatedAt()
    );
  }

  private RestaurantMembershipResponse toMembershipResponse(RestaurantMembershipEntity membership) {
    return new RestaurantMembershipResponse(
      membership.getId(),
      membership.getUser().getId(),
      membership.getUser().getFullName(),
      membership.getUser().getEmail(),
      membership.getRestaurant().getId(),
      membership.getRestaurant().getName(),
      membership.getRole(),
      membership.getCreatedAt()
    );
  }
}
