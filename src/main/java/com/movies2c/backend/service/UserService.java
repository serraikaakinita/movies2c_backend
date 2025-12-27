package com.movies2c.backend.service;

import com.movies2c.backend.model.User;
import com.movies2c.backend.model.dto.UpdateUserRequest;
import com.movies2c.backend.model.dto.ChangePasswordRequest;
import com.movies2c.backend.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(String id){
            return userRepository.findById(id);
    }

    public User updateUser(String id, UpdateUserRequest req){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getUserName() != null && !req.getUserName().isBlank()){
            user.setUserName(req.getUserName());
        }

        if (req.getEmail() != null && !req.getEmail().isBlank()){
            user.setEmail(req.getEmail());
        }

        if (req.getBio() != null){
            user.setBio(req.getBio());
        }

        if (req.getOldPassword() != null && !req.getOldPassword().isBlank() && req.getNewPassword() != null && !req.getNewPassword().isBlank()){
           if (!AuthenticationService.passwordMatches(req.getOldPassword(), user.getPasswordHash())){
               throw new RuntimeException("Current password is incorrect");
           }
           user.setPasswordHash(AuthenticationService.hash(req.getNewPassword()));
        }
        return userRepository.save(user);
    }

    public void changePassword(String id, ChangePasswordRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String currentPassword = req.getOldPassword();
        String newPassword = req.getNewPassword();

        if (!AuthenticationService.passwordMatches(currentPassword, user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new RuntimeException("New password must not be blank");
        }
        user.setPasswordHash(AuthenticationService.hash(newPassword));
        userRepository.save(user);
    }
}
