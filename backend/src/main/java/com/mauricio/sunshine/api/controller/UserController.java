package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.*;
import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import com.mauricio.sunshine.persistence.entity.UserEntity;
import com.mauricio.sunshine.service.UserService;
import com.mauricio.sunshine.service.PermissionService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class UserController {

  private final UserService userService;
  private final PermissionService permissionService;

  public UserController(UserService userService, PermissionService permissionService) {
    this.userService = userService;
    this.permissionService = permissionService;
  }

  @PostMapping("/users")
  public UserResponse createUser(
    @Valid @RequestBody CreateUserRequest req
  ) {
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
  public RestaurantMembershipResponse addUserToRestaurant(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @Valid @RequestBody AddUserToRestaurantRequest req
  ) {
    permissionService.requireOwner(userId, restaurantId);

    RestaurantMembershipEntity membership = userService.addUserToRestaurant(
      restaurantId,
      req.userId(),
      req.role()
    );

    return toMembershipResponse(membership);
  }

  @GetMapping("/restaurants/{restaurantId}/members")
  public List<RestaurantMembershipResponse> getMembersByRestautant(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId
  ) {

    permissionService.requireOwner(userId, restaurantId);

    return userService.getMembershipsByRestaurant(restaurantId)
    .stream()
    .map(this::toMembershipResponse)
    .toList();
  }

  @PatchMapping("/restaurants/{restaurantId}/members/{membershipId}/role")
  public RestaurantMembershipResponse updateMemberhsipRole(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID membershipId,
    @Valid @RequestBody UpdateMembershipRoleRequest req
  ){

    permissionService.requireOwner(userId, restaurantId);

    RestaurantMembershipEntity membership = userService.updateMembershipRole(
      restaurantId,
      membershipId,
      req.role()
    );

    return toMembershipResponse(membership);
  }

  @DeleteMapping("/restaurants/{restaurantId}/members/{membershipId}")
  public void removeMembership(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID membershipId
  ) {

    permissionService.requireOwner(userId, restaurantId);

    userService.removeMembership(restaurantId, membershipId);
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
