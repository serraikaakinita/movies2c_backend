package com.movies2c.backend.service;

import com.movies2c.backend.model.LoginRequest;
import com.movies2c.backend.model.SignupRequest;
import com.movies2c.backend.model.dto.ChangePasswordRequest;
import com.movies2c.backend.model.User;
import com.movies2c.backend.model.UserToken;
import com.movies2c.backend.repositories.UserRepository;
import com.movies2c.backend.repositories.UserTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.movies2c.backend.model.dto.LoginResponse;
import com.movies2c.backend.model.dto.UserDto;
import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;


@Service
public class AuthenticationService {

    // Secret key for signing the token
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTokenRepository userTokenRepository;
    private SecretKey jwtSecretKey;

    @PostConstruct
    public void init() {
        this.jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public static String hash(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    public static boolean passwordMatches(String rawPassword, String encoded) {
        return PASSWORD_ENCODER.matches(rawPassword, encoded);
    }

    public User signUp(SignupRequest request) {

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(hash(request.getPassword()));
        user.setDate(System.currentTimeMillis());
        // System.out.println(passwordHash);
        //user.setPasswordHash(passwordHash);
//        System.out.println(user.getUserName());
//        System.out.println(user.getEmail());
//        System.out.println(user.getPasswordHash());
//        System.out.println(user.getDate());


        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("invalid credentials");
        }

        User user = optionalUser.get();

        if (!passwordMatches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        Instant now = Instant.now();
        Instant exp = now.plus(3, ChronoUnit.HOURS);

        String jwt = Jwts.builder()
                .setSubject(user.getId())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("email", user.getEmail())
                .signWith(jwtSecretKey)
                .compact();
        UserToken userToken = new UserToken(jwt, user.getId());
        userTokenRepository.save(userToken);

        UserDto userDto = new UserDto(user.getId(), user.getUserName(), user.getEmail(), user.getBio(), user.getDate());
        return new LoginResponse(jwt, userDto);
    }

    public void changePassword(String token, ChangePasswordRequest request) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Missing token");
        }

        Optional<UserToken> optionalUserToken = userTokenRepository.findByToken(token);
        if (optionalUserToken.isEmpty()) {
            throw new RuntimeException("Invalid token");
        }

        String userId = optionalUserToken.get().getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordMatches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Wrong Current password");
        }
        user.setPasswordHash(hash(request.getNewPassword()));
        userRepository.save(user);
    }
    //LOGOUT
    public void logout(String token) {
        if (token == null || token.isBlank()) return;
        userTokenRepository.deleteByToken(token);
    }

    public User getUserFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Missing token");
        }
        Optional<UserToken> optionalUserToken = userTokenRepository.findByToken(token);
        if (optionalUserToken.isEmpty()) {
            throw new RuntimeException("Invalid or expired token");
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception ex) {
            userTokenRepository.deleteByToken(token);
            throw new RuntimeException("Token expired");
        }

        String userId = optionalUserToken.get().getUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Invalid user id");
        }
        userRepository.deleteById(id);
    }
}
