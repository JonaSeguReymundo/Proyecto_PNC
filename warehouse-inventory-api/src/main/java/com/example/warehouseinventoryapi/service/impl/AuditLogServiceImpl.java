package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.response.AuditLogResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.entity.AuditLog;
import com.example.warehouseinventoryapi.exception.BadRequestException;
import com.example.warehouseinventoryapi.exception.ResourceNotFoundException;
import com.example.warehouseinventoryapi.mapper.PageableMapper;
import com.example.warehouseinventoryapi.repository.AuditLogRepository;
import com.example.warehouseinventoryapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;
    private final PageableMapper     pageableMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String username, String action, String affectedTable, String detail) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username is required to record the audit log.");
        }
        if (action == null || action.isBlank()) {
            throw new BadRequestException("Action is required to record the audit log.");
        }
        if (affectedTable == null || affectedTable.isBlank()) {
            throw new BadRequestException("Affected table is required to record the audit log.");
        }

        repository.save(AuditLog.builder()
                .username(username)
                .action(action)
                .affectedTable(affectedTable)
                .detail(detail)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<AuditLogResponse> getAll(Pageable pageable) {
        Page<AuditLog> page = repository.findAll(pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No audit logs found in the system.");
        }
        return toPageable(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<AuditLogResponse> getByUsername(String username, Pageable pageable) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username for the search cannot be empty.");
        }

        Page<AuditLog> page = repository.findByUsername(username, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No audit logs found for user: " + username);
        }
        return toPageable(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<AuditLogResponse> getByTable(String table, Pageable pageable) {
        if (table == null || table.isBlank()) {
            throw new BadRequestException("Table name for the search cannot be empty.");
        }

        Page<AuditLog> page = repository.findByAffectedTable(table, pageable);

        if (page.isEmpty()) {
            throw new ResourceNotFoundException("No audit logs found for table: " + table);
        }
        return toPageable(page);
    }

    private PageableResponse<AuditLogResponse> toPageable(Page<AuditLog> page) {
        List<AuditLogResponse> content = page.getContent().stream()
                .map(log -> new AuditLogResponse(
                        log.getId(),
                        log.getUsername(),
                        log.getAction(),
                        log.getAffectedTable(),
                        log.getDetail(),
                        log.getCreatedAt()
                ))
                .toList();
        return pageableMapper.toPageableResponse(page, content);
    }
}