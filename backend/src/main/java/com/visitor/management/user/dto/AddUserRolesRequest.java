package com.visitor.management.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AddUserRolesRequest {

    @NotEmpty(message = "Danh sách vai trò không được để trống")
    private List<UUID> roleIds;
}