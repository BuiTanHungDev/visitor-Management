package com.visitor.management.user.service;

import com.visitor.management.user.dto.ChangePasswordRequest;
import com.visitor.management.user.dto.CreateUserRequest;
import com.visitor.management.user.dto.UpdateUserRequest;
import com.visitor.management.user.dto.UserResponse;
import com.visitor.management.user.entity.User;
import com.visitor.management.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Tạo tài khoản
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByUsernameIgnoreCase(
                request.getUsername().trim())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại");
        }

        String email = normalizeEmail(request.getEmail());

        if (email != null && userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        User user = new User();

        user.setUsername(request.getUsername().trim());
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPhone(normalizeText(request.getPhone()));
        user.setStatus("ACTIVE");

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    // Lấy tài khoản theo ID
    public UserResponse getUserById(UUID userId) {

        User user = findUserById(userId);

        return toResponse(user);
    }

    // Danh sách tài khoản có phân trang
    public Page<UserResponse> getAllUsers(Pageable pageable) {

        return userRepository.findAll(pageable)
                .map(this::toResponse);
    }

    // Cập nhật thông tin tài khoản
    @Transactional
    public UserResponse updateUser(
            UUID userId,
            UpdateUserRequest request
    ) {

        User user = findUserById(userId);

        String email = normalizeEmail(request.getEmail());

        if (email != null) {

            userRepository.findByEmailIgnoreCase(email)
                    .filter(existingUser ->
                            !existingUser.getId().equals(userId))
                    .ifPresent(existingUser -> {
                        throw new IllegalArgumentException(
                                "Email đã được sử dụng"
                        );
                    });
        }

        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPhone(normalizeText(request.getPhone()));

        return toResponse(userRepository.save(user));
    }

    // Đổi mật khẩu
    @Transactional
    public void changePassword(
            UUID userId,
            ChangePasswordRequest request
    ) {

        User user = findUserById(userId);

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPasswordHash()
        )) {
            throw new IllegalArgumentException(
                    "Mật khẩu hiện tại không chính xác"
            );
        }

        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPasswordHash()
        )) {
            throw new IllegalArgumentException(
                    "Mật khẩu mới phải khác mật khẩu hiện tại"
            );
        }

        user.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }

    // Khóa tài khoản
    @Transactional
    public UserResponse lockUser(UUID userId) {

        User user = findUserById(userId);

        user.setStatus("LOCKED");

        return toResponse(userRepository.save(user));
    }

    // Mở khóa tài khoản
    @Transactional
    public UserResponse unlockUser(UUID userId) {

        User user = findUserById(userId);

        if (!"LOCKED".equals(user.getStatus())) {
            throw new IllegalArgumentException(
                    "Tài khoản hiện không ở trạng thái bị khóa"
            );
        }

        user.setStatus("ACTIVE");

        return toResponse(userRepository.save(user));
    }

    // Tìm tài khoản hoặc báo lỗi
    private User findUserById(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy tài khoản: " + userId
                        )
                );
    }

    // Chuyển Entity sang Response DTO
    private UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private String normalizeEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    private String normalizeText(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}