package org.technoready.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.technoready.dto.request.CreateUserRequest;
import org.technoready.dto.response.ApiResponse;
import org.technoready.dto.response.UserResponse;
import org.technoready.entity.User;
import org.technoready.service.UserService;
import org.technoready.util.UserMapper;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;

/**

 User Controller
 Handles HTTP requests and responses for user operations
 Follows Single Responsibility Principle - only handles HTTP layer concerns
 Delegates business logic to UserService */
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Gson gson;

    /**
     * GET /users - Retrieve all users
     */
    public String getAllUsers(Request request, Response response) {
        log.info("GET /users - Fetching all users");

        try {
            List<User> users = userService.getAllUsers();
            List<UserResponse> userResponses = UserMapper.toResponseList(users);

            response.status(200);
            return gson.toJson(ApiResponse.success(userResponses));

        } catch (Exception e) {
            log.error("Error fetching users", e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to fetch users: " + e.getMessage()));
        }
    }

    /**
     * GET /users/:id - Retrieve user by ID
     */
    public String getUserById(Request request, Response response) {
        String idParam = request.params(":id");
        log.info("GET /users/{} - Fetching user by id", idParam);

        try {
            Long id = Long.parseLong(idParam);
            Optional<User> user = userService.getUserById(id);

            if (user.isPresent()) {
                UserResponse userResponse = UserMapper.toResponse(user.get());
                response.status(200);
                return gson.toJson(ApiResponse.success(userResponse));
            } else {
                response.status(404);
                return gson.toJson(ApiResponse.error("User not found with id: " + id));
            }

        } catch (NumberFormatException e) {
            log.error("Invalid user id format: {}", idParam);
            response.status(400);
            return gson.toJson(ApiResponse.error("Invalid user id format"));

        } catch (Exception e) {
            log.error("Error fetching user by id", e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to fetch user: " + e.getMessage()));
        }
    }

    /**
     * POST /users - Create new user
     */
    public String createUser(Request request, Response response) {
        log.info("POST /users - Creating new user");

        try {
            CreateUserRequest createRequest = gson.fromJson(request.body(), CreateUserRequest.class);

            if (createRequest == null) {
                response.status(400);
                return gson.toJson(ApiResponse.error("Request body is required"));
            }

            User user = userService.createUser(createRequest);
            UserResponse userResponse = UserMapper.toResponse(user);

            response.status(201);
            return gson.toJson(ApiResponse.success("User created successfully", userResponse));

        } catch (IllegalArgumentException e) {
            log.error("Validation error creating user", e);
            response.status(400);
            return gson.toJson(ApiResponse.error(e.getMessage()));

        } catch (Exception e) {
            log.error("Error creating user", e);
            response.status(500);
            return gson.toJson(ApiResponse.error("Failed to create user: " + e.getMessage()));
        }
    }
}