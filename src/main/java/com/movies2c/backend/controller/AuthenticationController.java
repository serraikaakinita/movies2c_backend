package com.movies2c.backend.controller;

import com.movies2c.backend.model.LoginRequest;
import com.movies2c.backend.model.SignupRequest;
import com.movies2c.backend.model.User;
import com.movies2c.backend.model.dto.ChangePasswordRequest;
import com.movies2c.backend.model.dto.LoginResponse;
import com.movies2c.backend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private  AuthenticationService authenticationService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> signup (@RequestBody SignupRequest request){
        try {
            User created = authenticationService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating user");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
        @RequestHeader(value = "Authorization", required = false) String authHeader,
        @RequestBody ChangePasswordRequest request){

        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Authorization Header");
        }
        String token = authHeader.replace("Bearer ", "").trim();

        try {
            authenticationService.changePassword(token, request);
            return ResponseEntity.ok("Password changed");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(value = "Authorization", required = false) String authHeader){
        if (authHeader == null || authHeader.isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing Authorization Header");
        }
        String token = authHeader.replace("Bearer ", "").trim();
        authenticationService.logout(token);
        return ResponseEntity.ok().build();
    }
}
