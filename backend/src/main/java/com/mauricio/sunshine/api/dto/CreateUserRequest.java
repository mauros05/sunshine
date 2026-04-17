package com.mauricio.sunshine.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

  @NotBlank
  @JsonAlias("full_name")
  String fullName,

  @NotBlank
  @Email
  String email
) {}
