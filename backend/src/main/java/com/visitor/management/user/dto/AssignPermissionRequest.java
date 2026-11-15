package com.visitor.management.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignPermissionRequest {

    @NotNull(message = "ID quyền hạn không được để trống")
    private UUID permissionId;
}