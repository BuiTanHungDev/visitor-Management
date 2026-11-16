
package com.visitor.management.user.controller;

import com.visitor.management.security.service.TokenRevocationService;
import com.visitor.management.user.dto.CurrentUserResponse;
import com.visitor.management.user.dto.LoginRequest;
import com.visitor.management.user.dto.LoginResponse;
import com.visitor.management.user.dto.LogoutResponse;
import com.visitor.management.user.entity.User;
import com.visitor.management.user.service.AuthenticationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenRevocationService tokenRevocationService;

    /**
     * API đăng nhập bằng username hoặc email.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authenticationService.authenticate(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Lấy thông tin tài khoản đang đăng nhập.
     */
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @AuthenticationPrincipal User user) {

        CurrentUserResponse response =
                CurrentUserResponse.from(user);

        return ResponseEntity.ok(response);
    }

    /**
     * Đăng xuất và thu hồi Access Token.
     */
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {

        String token = authorization.substring(7);

        tokenRevocationService.revokeToken(token);

        return ResponseEntity.ok(
                new LogoutResponse("Đăng xuất thành công")
        );
    }
}
