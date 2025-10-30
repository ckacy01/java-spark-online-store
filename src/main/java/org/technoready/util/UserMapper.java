package org.technoready.util;



import org.technoready.dto.request.CreateUserRequest;
import org.technoready.dto.response.UserResponse;
import org.technoready.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User Mapper Utility
 * Maps between Entity and DTO objects
 * Follows Single Responsibility Principle - only handles mapping
 */
public class UserMapper {

    /**
     * Convert CreateUserRequest to User entity
     */
    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    /**
     * Convert User entity to UserResponse DTO
     */
    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Convert list of User entities to list of UserResponse DTOs
     */
    public static List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }
}
