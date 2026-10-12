package app.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Locale;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import app.dto.AuthResponse;
import app.dto.LoginRequest;
import app.dto.RegisterRequest;
import app.model.User;
import app.repository.UserRepository;
import app.security.JwtService;

@Service
public class UserService {
    private static final String DUMMY_BCRYPT_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User registerUser(RegisterRequest request) {
        if (request.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }
        String email = normalizeEmail(request.getEmail());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }
        User user = new User(request.getName().trim(), email, passwordEncoder.encode(request.getPassword()));
        try {
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
            }
            throw exception;
        }
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getPassword().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BadCredentialsException("Invalid credentials");
        }
        Optional<User> found = userRepository.findByEmail(normalizeEmail(request.getEmail()));
        String candidateHash = found.map(User::getPassword).orElse(DUMMY_BCRYPT_HASH);
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), candidateHash);
        User user = found.orElse(null);
        if (user == null || !passwordMatches) throw new BadCredentialsException("Invalid credentials");
        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new java.util.NoSuchElementException("User not found"));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

