package com.example.common.web.exception;

import java.time.Instant;
import java.util.List;

/**
 * Standard error response shape returned to API clients.
 * Stable across all services; do not change without versioning.
 */
public record ErrorResponse(
        String code,
        String message,
        Instant timestamp,
        List<FieldError> fieldErrors
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, Instant.now(), List.of());
    }

    public static ErrorResponse of(String code, String message, List<FieldError> fieldErrors) {
        return new ErrorResponse(code, message, Instant.now(), fieldErrors);
    }

    public record FieldError(String field, String message) {}
}
