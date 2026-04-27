package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.RestaurantMembershipEntity;
import com.mauricio.sunshine.persistence.entity.UserRole;
import com.mauricio.sunshine.persistence.repository.RestaurantMembershipRepository;
import com.mauricio.sunshine.ForbiddenActionException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PermissionService {

  private final RestaurantMembershipRepository membershipRepo;

  public PermissionService(RestaurantMembershipRepository membershipRepo) {
    this.membershipRepo = membershipRepo;
  }

  public RestaurantMembershipEntity requireMembership(UUID userId, UUID restaurantId) {
    return membershipRepo.findByUserIdAndRestaurantId(userId, restaurantId)
    .orElseThrow(() -> new ForbiddenActionException("User doesn't belong to this restaurant"));
  }

  public void requireAnyRole(UUID userId, UUID restaurantId, UserRole... allowedRoles) {
    RestaurantMembershipEntity membership = requireMembership(userId, restaurantId);

    for (UserRole role : allowedRoles) {
      if (membership.getRole() == role) {
        return;
      }
    }

    throw new ForbiddenActionException("User does not have permission for this action");
  }

  public void requireOwner(UUID userId, UUID restaurantId) {
    requireAnyRole(userId, restaurantId, UserRole.OWNER);
  }

  public void requireOwnerOrManager(UUID userId, UUID restaurantId) {
    requireAnyRole(userId, restaurantId, UserRole.OWNER, UserRole.MANAGER);
  }

  public void requireOwnerOrManagerOrCashier(UUID userId, UUID restaurantId) {
    requireAnyRole(userId, restaurantId, UserRole.OWNER, UserRole.MANAGER, UserRole.CASHIER);
  }
}
