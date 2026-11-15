package com.visitor.management.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePermissionRequest {

    @NotBlank(message = "Mã quyền hạn không được để trống")
    @Size(max = 100, message = "Mã quyền hạn tối đa 100 ký tự")
    private String code;

    @NotBlank(message = "Tên quyền hạn không được để trống")
    @Size(max = 150, message = "Tên quyền hạn tối đa 150 ký tự")
    private String name;

    @NotBlank(message = "Module không được để trống")
    @Size(max = 50, message = "Module tối đa 50 ký tự")
    private String module;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    private String description;
}