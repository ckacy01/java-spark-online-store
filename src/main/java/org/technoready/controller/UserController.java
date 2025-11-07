package org.technoready.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.technoready.dto.request.CreateUserRequest;
import org.technoready.dto.request.UpdateUserRequest;
import org.technoready.dto.response.ApiResponse;
import org.technoready.dto.response.UserResponse;
import org.technoready.entity.User;
import org.technoready.exception.BadRequestException;
import org.technoready.exception.NotFoundException;
import org.technoready.service.UserService;
import org.technoready.util.UserMapper;
import spark.Request;
import spark.Response;

import java.util.List;

/**

 User Controller
 Handles HTTP requests and responses for user operations
 Follows Single Responsibility Principle - only handles HTTP layer concerns
 Delegates business logic to UserService
 */
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
        List<UserResponse> users = UserMapper.toResponseList(userService.getAllUsers());
        response.status(200);
        return gson.toJson(ApiResponse.success(users));
    }

    /**
     * GET /users/:id - Retrieve user by ID
     */
    public String getUserById(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("GET /users/{} - Fetching user by id", id);

        User user = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        response.status(200);
        return gson.toJson(ApiResponse.success(UserMapper.toResponse(user)));
    }

    /**
     * POST /users - Create new user
     */
    public String createUser(Request request, Response response) {
        log.info("POST /users - Creating new user");
        CreateUserRequest body = gson.fromJson(request.body(), CreateUserRequest.class);
        if (body == null) throw new BadRequestException("Invalid request body");
        User user = userService.createUser(body);
        response.status(201);
        return gson.toJson(ApiResponse.success("User created successfully", UserMapper.toResponse(user)));
    }

    /**
     * PUT /users/:id - Update user
     */
    public String updateUser(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("PUT /users/{} - Updating user", id);

        UpdateUserRequest body = gson.fromJson(request.body(), UpdateUserRequest.class);
        if (body == null) throw new BadRequestException("Invalid request body");

        User user = userService.updateUser(id, body);
        response.status(200);
        return gson.toJson(ApiResponse.success("User updated successfully", UserMapper.toResponse(user)));
    }

    /**
     * DELETE /users/:id - Delete user
     */
    public String deleteUser(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("DELETE /users/{} - Deleting user", id);
        userService.deleteUser(id);
        response.status(200);
        return gson.toJson(ApiResponse.success("User deleted successfully"));
    }

    /**
     * OPTIONS /users/:id - Check if user exists
     */
    public String checkUserExists(Request request, Response response) {
        Long id = Long.parseLong(request.params(":id"));
        log.info("GET /users/{} - Checking if user exists", id);
        boolean exists = userService.userExists(id);
        response.status(200);
        return gson.toJson(ApiResponse.success("User existence checked", exists));

    }
}