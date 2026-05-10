package com.example.habits.api.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.web.exception.ErrorResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthService.TokenResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(req.email(), req.password(), req.displayName()));
    }

    @PostMapping("/login")
    public AuthService.TokenResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req.email(), req.password());
    }

    @ExceptionHandler(AuthService.EmailAlreadyRegisteredException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyRegistered() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("EMAIL_ALREADY_REGISTERED", "Email already registered"));
    }

    @ExceptionHandler(AuthService.InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalid() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("INVALID_CREDENTIALS", "Invalid email or password"));
    }

    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 8, max = 200) String password,
            @NotBlank @Size(max = 100) String displayName
    ) {}

    public record LoginRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}
}
