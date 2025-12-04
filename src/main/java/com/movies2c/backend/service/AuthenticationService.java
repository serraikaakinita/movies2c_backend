package com.movies2c.backend.service;

import com.movies2c.backend.model.SignupRequest;
import com.movies2c.backend.model.User;
import com.movies2c.backend.model.LoginRequest;
import com.movies2c.backend.model.UserToken;
import com.movies2c.backend.repositories.UserRepository;
import com.movies2c.backend.repositories.UserTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.sql.Time;


@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserTokenRepository userTokenRepository;

    // Secret key for signing the token
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token validity: 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Generate JWT token
    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Optional: parse token
    public static String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    @Autowired
    public AuthenticationService(UserRepository userRepository,UserTokenRepository userTokenRepository){
        this.userRepository=userRepository;
        this.userTokenRepository= userTokenRepository;

    }

    public static String hash(String password){
       return encoder.encode(password);

    }

    public static boolean passwordMatches(String password,String passwordHash){
        return encoder.matches(password,passwordHash);
    }

    public User createUser(SignupRequest signupRequest){
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setEmail(signupRequest.getEmail());
        String passwordHash=hash(signupRequest.getPassword());
        // System.out.println(passwordHash);
        user.setPasswordHash(passwordHash);
        user.setDate(System.currentTimeMillis());
//        System.out.println(user.getUserName());
//        System.out.println(user.getEmail());
//        System.out.println(user.getPasswordHash());
//        System.out.println(user.getDate());


        return userRepository.save(user);
    }



    public String login(String email,String password){
        User user = userRepository.findByEmail(email).get();
        System.out.println(user.getUserName());
        if(passwordMatches(password,user.getPasswordHash())){
            System.out.println("Correct Password");
        }else {
            System.out.println("Incorrect Password");
        }

        String token=generateToken(email);
        UserToken userToken= new UserToken(token,user.getId());
        userTokenRepository.save(userToken);

        return token;
    }
    public ResponseEntity<?> deleteUser(String id){
        if(!userRepository.existsById(id)){
            return  ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return  ResponseEntity.ok().build();
    }




}
