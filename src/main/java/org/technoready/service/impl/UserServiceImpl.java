package org.technoready.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.technoready.dao.UserDao;
import org.technoready.dto.request.CreateUserRequest;
import org.technoready.dto.request.UpdateUserRequest;
import org.technoready.entity.User;
import org.technoready.exception.ConflictException;
import org.technoready.exception.NotFoundException;
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

        request.validate();

        Optional<User> existingUser = jdbi.withExtension(UserDao.class,
                dao -> dao.findByUsername(request.getUsername()));

        if (existingUser.isPresent()) {
            throw new ConflictException("Username already exists: " + request.getUsername());
        }

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

    @Override
    public User updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);


        request.validate();

        User user = getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        User updatedUser = UserMapper.updateEntity(user, request);
        jdbi.useExtension(UserDao.class, dao -> dao.update(updatedUser));

        log.info("User updated successfully with id: {}", id);
        return updatedUser;
    }

    @Override
    public boolean deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);

        int rowsAffected = jdbi.withExtension(UserDao.class, dao -> dao.delete(id));

        if (rowsAffected == 0) {
            throw new NotFoundException("User not found with id: " + id);
        }

        log.warn("User deleted successfully with id: {}", id);
        return true;
    }

    @Override
    public boolean userExists(Long id) {
        log.debug("Checking if user exists with id: {}", id);
        return jdbi.withExtension(UserDao.class, dao -> dao.exists(id));
    }
}
