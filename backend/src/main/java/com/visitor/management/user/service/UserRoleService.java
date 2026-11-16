
package com.visitor.management.user.service;

import com.visitor.management.user.dto.AddUserRolesRequest;
import com.visitor.management.user.dto.UserRoleResponse;
import com.visitor.management.user.entity.Role;
import com.visitor.management.user.entity.User;
import com.visitor.management.user.entity.UserRole;
import com.visitor.management.user.entity.UserRoleId;
import com.visitor.management.user.repository.RoleRepository;
import com.visitor.management.user.repository.UserRepository;
import com.visitor.management.user.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    // Lấy danh sách vai trò của User
    public List<UserRoleResponse> getUserRoles(UUID userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy người dùng: " + userId
            );
        }

        return userRoleRepository
                .findAllByUserIdWithRole(userId)
                .stream()
                .map(UserRoleResponse::from)
                .toList();
    }

    // Gán thêm vai trò cho User
    @Transactional
    public List<UserRoleResponse> addUserRoles(
            UUID userId,
            AddUserRolesRequest request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy người dùng: " + userId
                        )
                );

        List<UUID> roleIds = request.getRoleIds();

        // Kiểm tra ID trùng trong request
        Set<UUID> uniqueIds = new HashSet<>(roleIds);

        if (uniqueIds.size() != roleIds.size()) {
            throw new IllegalArgumentException(
                    "Danh sách vai trò chứa ID trùng lặp"
            );
        }

        // Kiểm tra các Role có tồn tại
        List<Role> roles = roleRepository.findAllById(roleIds);

        if (roles.size() != roleIds.size()) {

            Set<UUID> foundIds = roles.stream()
                    .map(Role::getId)
                    .collect(Collectors.toSet());

            List<UUID> missingIds = roleIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            throw new IllegalArgumentException(
                    "Không tìm thấy các vai trò: " + missingIds
            );
        }

        // Chỉ thêm các Role User chưa có
        List<UserRole> newUserRoles = roles.stream()
                .filter(role ->
                        !userRoleRepository.existsByUser_IdAndRole_Id(
                                userId,
                                role.getId()
                        )
                )
                .map(role -> {

                    UserRoleId id = new UserRoleId();
                    id.setUserId(user.getId());
                    id.setRoleId(role.getId());

                    UserRole userRole = new UserRole();
                    userRole.setId(id);
                    userRole.setUser(user);
                    userRole.setRole(role);

                    return userRole;
                })
                .toList();

        userRoleRepository.saveAllAndFlush(newUserRoles);

        return getUserRoles(userId);
    }

    // Thu hồi một Role khỏi User
    @Transactional
    public List<UserRoleResponse> removeUserRole(
            UUID userId,
            UUID roleId
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy người dùng: " + userId
                        )
                );

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy vai trò: " + roleId
                        )
                );

        boolean assigned =
                userRoleRepository.existsByUser_IdAndRole_Id(
                        userId,
                        roleId
                );

        if (!assigned) {
            throw new IllegalArgumentException(
                    "Người dùng chưa được gán vai trò này"
            );
        }

        // Bảo vệ ADMIN cuối cùng đang hoạt động
        if ("ADMIN".equalsIgnoreCase(role.getCode())
                && "ACTIVE".equalsIgnoreCase(user.getStatus())) {

            long activeAdminCount =
                    userRoleRepository
                            .countByRole_CodeIgnoreCaseAndUser_StatusIgnoreCase(
                                    "ADMIN",
                                    "ACTIVE"
                            );

            if (activeAdminCount <= 1) {
                throw new IllegalArgumentException(
                        "Không thể thu hồi vai trò ADMIN "
                                + "của quản trị viên đang hoạt động cuối cùng"
                );
            }
        }

        userRoleRepository.deleteByUser_IdAndRole_Id(
                userId,
                roleId
        );

        return getUserRoles(userId);
    }
}
