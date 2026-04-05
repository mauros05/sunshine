package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.LoginRequest;
import com.mauricio.sunshine.api.dto.LoginResponse;
import com.mauricio.sunshine.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest req) {
    return authService.loginByEmail(req.email());
  }
}
