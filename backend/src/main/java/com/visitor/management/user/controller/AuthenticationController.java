
package com.visitor.management.user.controller;

import com.visitor.management.user.dto.LoginRequest;
import com.visitor.management.user.dto.LoginResponse;
import com.visitor.management.user.service.AuthenticationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

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
}
