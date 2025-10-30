package org.technoready.service;

import org.technoready.dto.request.CreateUserRequest;
import org.technoready.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * User Service Interface
 * Defines business logic contract for user operations
 * Follows Interface Segregation Principle - clients depend on abstractions
 * Allows for easy mocking in tests and multiple implementations
 */
public interface UserService {

    /**
     * Retrieve all users
     */
    List<User> getAllUsers();
    /**
     * Create a new user
     */
    User createUser(CreateUserRequest request);
    /**
     * Retrieve an user by id
     */
    Optional<User> getUserById(Long id);
}