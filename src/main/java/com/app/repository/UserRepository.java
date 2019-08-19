package com.app.repository;

import com.app.entity.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String>{

    /**
     * Finds user by email
     * @param email to look for
     * @return user by given email
     */
    User findByEmail(String email);

    /**
     * Finds user by name
     * @param name to look for
     * @return user by given name
     */
    User findByName(String name);
}
