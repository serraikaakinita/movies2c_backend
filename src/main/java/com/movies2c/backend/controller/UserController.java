package com.movies2c.backend.controller;

import com.movies2c.backend.model.User;
import com.movies2c.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService= userService;
    }


    @GetMapping("/{id}")
    public  ResponseEntity<User> getUser(@PathVariable String id)
    {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()){
            return  ResponseEntity.ok(user.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
