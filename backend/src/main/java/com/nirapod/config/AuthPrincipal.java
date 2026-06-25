package com.nirapod.config;

public class AuthPrincipal {

  private final String email;
  private final Long userId;
  private final String role;

  public AuthPrincipal(String email, Long userId, String role) {
    this.email = email;
    this.userId = userId;
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public Long getUserId() {
    return userId;
  }

  public String getRole() {
    return role;
  }

  @Override
  public String toString() {
    return email;
  }
}