package com.mauricio.sunshine.api.dto;

import java.util.List;
import java.util.UUID;

public record LoginResponse(
  UUID userId,
  String fullName,
  String email,
  List<LoginMembershipResponse> memberships
) {}
