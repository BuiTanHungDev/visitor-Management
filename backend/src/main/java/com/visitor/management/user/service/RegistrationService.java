
package com.visitor.management.user.service;

import com.visitor.management.user.dto.RegisterRequest;
import com.visitor.management.user.dto.RegisterResponse;
import com.visitor.management.user.entity.Role;
import com.visitor.management.user.entity.User;
import com.visitor.management.user.entity.UserRole;
import com.visitor.management.user.entity.UserRoleId;
import com.visitor.management.user.repository.RoleRepository;
import com.visitor.management.user.repository.UserRepository;
import com.visitor.management.user.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistrationService {

    private static final String DEFAULT_ROLE = "RELATIVE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Đăng ký tài khoản thân nhân, không yêu cầu OTP.
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String email = normalizeEmail(request.getEmail());
        String phone = normalizePhone(request.getPhone());

        // Bắt buộc chọn đúng một phương thức đăng ký.
        if ((email == null && phone == null)
                || (email != null && phone != null)) {

            throw new IllegalArgumentException(
                    "Vui lòng cung cấp email hoặc số điện thoại, chỉ chọn một."
            );
        }

        // Kiểm tra email trùng.
        if (email != null
                && userRepository.existsByEmailIgnoreCase(email)) {

            throw new IllegalArgumentException(
                    "Email đã được sử dụng."
            );
        }

        // Kiểm tra số điện thoại trùng.
        if (phone != null
                && userRepository.existsByPhone(phone)) {

            throw new IllegalArgumentException(
                    "Số điện thoại đã được sử dụng."
            );
        }

        // Tìm Role mặc định dành cho thân nhân.
        Role relativeRole = roleRepository
                .findByCodeIgnoreCase(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException(
                        "Không tìm thấy Role mặc định: " + DEFAULT_ROLE
                ));

        // Tạo tài khoản.
        User user = new User();

        user.setUsername(null);
        user.setFullName(request.getFullName().trim());
        user.setEmail(email);
        user.setPhone(phone);

        // Chưa yêu cầu xác thực OTP.
        user.setEmailVerifiedAt(null);
        user.setPhoneVerifiedAt(null);

        // Mã hóa mật khẩu BCrypt.
        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setStatus("ACTIVE");

        User savedUser = userRepository.save(user);

        // Tạo khóa chính ghép UserRoleId.
        UserRoleId userRoleId = new UserRoleId();
        userRoleId.setUserId(savedUser.getId());
        userRoleId.setRoleId(relativeRole.getId());

        // Tạo quan hệ User - Role.
        UserRole userRole = new UserRole();
        userRole.setId(userRoleId);
        userRole.setUser(savedUser);
        userRole.setRole(relativeRole);

        userRoleRepository.save(userRole);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getStatus(),
                "Đăng ký tài khoản thành công.",
                savedUser.getCreatedAt()
        );
    }

    private String normalizeEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizePhone(String phone) {

        if (phone == null || phone.isBlank()) {
            return null;
        }

        return phone.trim();
    }
}
