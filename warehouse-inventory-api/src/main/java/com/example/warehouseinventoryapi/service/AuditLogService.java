package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.response.AuditLogResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    void record(String username, String action, String affectedTable, String detail);
    PageableResponse<AuditLogResponse> getAll(Pageable pageable);
    PageableResponse<AuditLogResponse> getByUsername(String username, Pageable pageable);
    PageableResponse<AuditLogResponse> getByTable(String table, Pageable pageable);
}
