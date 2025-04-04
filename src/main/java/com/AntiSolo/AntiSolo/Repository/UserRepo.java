package com.AntiSolo.AntiSolo.Repository;

import com.AntiSolo.AntiSolo.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends MongoRepository<User,String> {

    Optional<User> findByEmail(String email);

    @Query("{ '_id': { $regex: ?0, $options: 'i' } }") // Case-insensitive prefix search
    List<User> findByUserNamePrefix(String prefix);

}
