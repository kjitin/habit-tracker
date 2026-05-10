package com.example.habits.api.auth;

import com.example.common.security.jwt.JwtService;
import com.example.habits.domain.user.User;
import com.example.habits.domain.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponse register(String email, String password, String displayName) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyRegisteredException();
        }
        String hash = passwordEncoder.encode(password);
        User saved = userRepository.save(User.create(email, hash, displayName));
        return new TokenResponse(jwtService.issueAccessToken(saved.getId(), saved.getEmail()));
    }

    public TokenResponse login(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
        return new TokenResponse(jwtService.issueAccessToken(user.getId(), user.getEmail()));
    }

    public record TokenResponse(String token) {}

    public static class EmailAlreadyRegisteredException extends RuntimeException {}
    public static class InvalidCredentialsException extends RuntimeException {}
}
