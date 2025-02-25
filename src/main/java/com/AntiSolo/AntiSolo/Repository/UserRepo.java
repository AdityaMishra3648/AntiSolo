package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
}
