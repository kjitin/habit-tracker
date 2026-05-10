package com.example.common.security.principal;

import java.util.UUID;

/**
 * Principal stored in the SecurityContext after JWT verification.
 * Use as Authentication.getPrincipal() == AuthenticatedUser.
 */
public record AuthenticatedUser(UUID userId, String email) {
}
