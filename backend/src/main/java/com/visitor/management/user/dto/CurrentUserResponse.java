
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
    private List<String> permissions;

    public static CurrentUserResponse from(User user) {

        // Lấy danh sách Role.
        List<String> roles = user.getUserRoles()
                .stream()
                .map(userRole -> userRole.getRole().getCode())
                .distinct()
                .sorted()
                .toList();

        // Lấy danh sách Permission từ các Role.
        List<String> permissions = user.getUserRoles()
                .stream()
                .flatMap(userRole ->
                        userRole.getRole()
                                .getRolePermissions()
                                .stream()
                )
                .map(rolePermission ->
                        rolePermission.getPermission().getCode()
                )
                .distinct()
                .sorted()
                .toList();

        return CurrentUserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .status(user.getStatus())
                .roles(roles)
                .permissions(permissions)
                .build();
    }
}
