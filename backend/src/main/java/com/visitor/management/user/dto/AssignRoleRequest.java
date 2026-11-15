package com.visitor.management.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignRoleRequest {

    @NotNull(message = "ID vai trò không được để trống")
    private UUID roleId;
}