package com.visitor.management.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UpdateRolePermissionsRequest {

    @NotNull(message = "Danh sách quyền hạn không được để trống")
    private List<UUID> permissionIds;
}