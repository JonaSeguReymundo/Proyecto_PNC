package com.example.warehouseinventoryapi.service.impl;

import com.example.warehouseinventoryapi.dto.response.AuditLogResponse;
import com.example.warehouseinventoryapi.dto.response.PageableResponse;
import com.example.warehouseinventoryapi.entity.AuditLog;
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

    /**
     * Guarda un registro de auditoría.
     * Usa REQUIRES_NEW para que el log siempre se guarde
     * aunque la transacción principal falle (ej: para registrar intentos fallidos).
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String username, String action, String affectedTable, String detail) {
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
        return toPageable(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<AuditLogResponse> getByUsername(String username, Pageable pageable) {
        Page<AuditLog> page = repository.findByUsername(username, pageable);
        return toPageable(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageableResponse<AuditLogResponse> getByTable(String table, Pageable pageable) {
        Page<AuditLog> page = repository.findByAffectedTable(table, pageable);
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
