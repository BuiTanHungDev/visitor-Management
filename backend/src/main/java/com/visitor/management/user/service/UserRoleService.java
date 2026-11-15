package com.visitor.management.user.service;

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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    // Gán vai trò cho người dùng
    @Transactional
    public void assignRole(UUID userId, UUID roleId) {

        User user = findUser(userId);
        Role role = findRole(roleId);

        if (userRoleRepository.existsByUser_IdAndRole_Id(userId, roleId)) {
            throw new IllegalArgumentException(
                    "Người dùng đã được gán vai trò này"
            );
        }

        UserRole userRole = new UserRole();

        UserRoleId id = new UserRoleId();
        id.setUserId(userId);
        id.setRoleId(roleId);

        userRole.setId(id);
        userRole.setUser(user);
        userRole.setRole(role);

        userRoleRepository.save(userRole);
    }

    // Thu hồi vai trò khỏi người dùng
    @Transactional
    public void removeRole(UUID userId, UUID roleId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy người dùng"
            );
        }

        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException(
                    "Không tìm thấy vai trò"
            );
        }

        if (!userRoleRepository.existsByUser_IdAndRole_Id(userId, roleId)) {
            throw new IllegalArgumentException(
                    "Người dùng chưa được gán vai trò này"
            );
        }

        userRoleRepository.deleteByUser_IdAndRole_Id(userId, roleId);
    }

    // Lấy danh sách vai trò của người dùng
    public List<Role> getUserRoles(UUID userId) {

        User user = findUser(userId);

        return user.getUserRoles()
                .stream()
                .map(UserRole::getRole)
                .toList();
    }

    private User findUser(UUID userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy người dùng: " + userId
                        )
                );
    }

    private Role findRole(UUID roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy vai trò: " + roleId
                        )
                );
    }
}