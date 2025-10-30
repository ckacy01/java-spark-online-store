package org.technoready.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.dao.UserDao;
import org.technoready.dto.request.CreateUserRequest;
import org.technoready.entity.User;
import org.technoready.service.UserService;
import org.technoready.util.UserMapper;

import java.util.List;
import java.util.Optional;

/**
 * User Service Implementation
 * Contains business logic for user operations
 */
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Jdbi jdbi;

    @Override
    public List<User> getAllUsers() {
        log.debug("Fetching all users");
        return jdbi.withExtension(UserDao.class, UserDao::findAll);
    }

    @Override
    public User createUser(CreateUserRequest request) {
        log.info("Creating new user with username: {}", request.getUsername());

        // Validate request
        request.validate();

        // Check if username already exists
        Optional<User> existingUser = jdbi.withExtension(UserDao.class,
                dao -> dao.findByUsername(request.getUsername()));

        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        // Map and insert
        User user = UserMapper.toEntity(request);
        long generatedId = jdbi
                .withExtension(UserDao.class, dao -> dao.insert(user));
        user.setId(generatedId);

        log.info("User created successfully with id: {}", generatedId);
        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.debug("Fetching user with id: {}", id);
        return jdbi.withExtension(UserDao.class, dao -> dao.findById(id));
    }
}
