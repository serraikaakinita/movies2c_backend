package com.movies2c.backend.controller;

import com.movies2c.backend.model.SignupRequest;
import com.movies2c.backend.model.User;
import com.movies2c.backend.model.LoginRequest;
import com.movies2c.backend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @Autowired
    public  AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService=authenticationService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        String response = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return  response;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser (@RequestBody SignupRequest signupRequest){

        try{
            return  ResponseEntity.ok(authenticationService.createUser(signupRequest));
        }
        catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating User");
        }
    }


}
