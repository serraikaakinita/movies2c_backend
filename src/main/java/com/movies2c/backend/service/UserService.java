package com.movies2c.backend.service;

import com.movies2c.backend.model.User;
import com.movies2c.backend.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository= userRepository;
    }
    public Optional<User> getUserById(String id){
        Optional<User> user=userRepository.findById(id);
        if(user.isPresent()){
            return user;
        }
        else {
            return Optional.empty();
        }
    }



}
