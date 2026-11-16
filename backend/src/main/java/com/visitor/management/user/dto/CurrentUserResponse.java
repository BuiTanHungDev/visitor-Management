package com.visitor.management.user.dto;

import com.visitor.management.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CurrentUserResponse {

    private UUID userId;
    private String username;
    private String fullName;
    private String email;
    private String status;
    private List<String> roles;

    public static CurrentUserResponse from(User user) {
        List<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getCode())
                .sorted()
                .toList();

        return CurrentUserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .status(user.getStatus())
                .roles(roles)
                .build();
    }
}