package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateUserRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateUserRequest;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponse create(CreateUserRequest request);
    UserResponse update(Long id, UpdateUserRequest request);
    UserResponse getById(Long id);
    void delete(Long id);
    PageableResponse<UserResponse> getAllActive(Pageable pageable);
}
