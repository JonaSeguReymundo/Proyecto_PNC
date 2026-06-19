package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.request.CreateUserRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateUserRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.UserResponse;
import com.example.warehouseinventoryapi.entity.Role;
import com.example.warehouseinventoryapi.entity.RoleName;
import com.example.warehouseinventoryapi.entity.User;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.repository.RoleRepository;
import com.example.warehouseinventoryapi.repository.UserRepository;
import com.example.warehouseinventoryapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository  userRepository;
    private final RoleRepository  roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PageableMapper  pageableMapper;

    @Override
    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }

        Set<Role> roles = resolveRoles(request.roles());

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .roles(roles)
                .active(true)
                .build();

        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = findOrThrow(id);

        user.setFullName(request.fullName());
        user.setRoles(resolveRoles(request.roles()));
        if (request.active() != null) user.setActive(request.active());

        return toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return toDto(findOrThrow(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = findOrThrow(id);
        // Soft delete
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<UserResponse> getAllActive(Pageable pageable) {
        Page<User> page = userRepository.findAllByActiveTrue(pageable);
        List<UserResponse> content = page.getContent().stream().map(this::toDto).toList();
        return pageableMapper.toPageableResponse(page, content);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private Set<Role> resolveRoles(Set<RoleName> roleNames) {
        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name)))
                .collect(Collectors.toSet());
    }

    private UserResponse toDto(User user) {
        Set<RoleName> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getActive(),
                roleNames
        );
    }
}
