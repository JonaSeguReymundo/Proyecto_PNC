package com.example.warehouseinventoryapi.audit;

import com.example.warehouseinventoryapi.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Aspecto AOP que intercepta los métodos de escritura de los services
 * y registra automáticamente en audit_log.
 *
 * Pointcuts cubren los métodos create, update y delete de todos los
 * ServiceImpl dentro del paquete service.impl.
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogService auditLogService;

    @AfterReturning("execution(* com.example.warehouseinventoryapi.service.impl.*ServiceImpl.create(..))")
    public void auditCreate(JoinPoint jp) {
        audit(jp, "CREATE");
    }

    @AfterReturning("execution(* com.example.warehouseinventoryapi.service.impl.*ServiceImpl.update(..))")
    public void auditUpdate(JoinPoint jp) {
        audit(jp, "UPDATE");
    }

    @AfterReturning("execution(* com.example.warehouseinventoryapi.service.impl.*ServiceImpl.delete(..))")
    public void auditDelete(JoinPoint jp) {
        audit(jp, "DELETE");
    }

    private void audit(JoinPoint jp, String action) {
        try {
            String username  = resolveUsername();
            String className = jp.getTarget().getClass().getSimpleName();
            // "ProductServiceImpl" → "products"
            String table = toTableName(className);
            String detail = "Method: " + jp.getSignature().getName()
                    + " | Args count: " + jp.getArgs().length;

            auditLogService.record(username, action, table, detail);
        } catch (Exception ex) {
            // No romper el flujo principal si la auditoría falla
            log.error("Audit aspect error: {}", ex.getMessage());
        }
    }

    private String resolveUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "anonymous";
    }

    /** ProductServiceImpl → products, WarehouseServiceImpl → warehouses, etc. */
    private String toTableName(String serviceClassName) {
        return serviceClassName
                .replace("ServiceImpl", "")
                .replaceAll("([A-Z])", "_$1")
                .toLowerCase()
                .replaceFirst("^_", "")
                + "s";
    }
}
