package com.movies2c.backend.repositories;

import com.movies2c.backend.model.UserToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserTokenRepository extends MongoRepository<UserToken,String> {
}
