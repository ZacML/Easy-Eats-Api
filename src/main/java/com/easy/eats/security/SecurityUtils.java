package com.easy.eats.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.easy.eats.usuario.model.Role;

public class SecurityUtils {

    private SecurityUtils() {
    }

    public static AuthenticatedUser getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof AuthenticatedUser authenticatedUser)) {
            throw new IllegalStateException("Usuário não autenticado");
        }
        return authenticatedUser;
    }

    public static boolean isSuperadmin() {
        return getUsuarioAutenticado().getRole() == Role.SUPERADMIN;
    }

    /**
     * Empresa do usuário autenticado. Nula apenas para SUPERADMIN, que enxerga
     * todas as empresas; nesse caso os services não devem filtrar por empresa.
     */
    public static Integer getEmpresaId() {
        Integer empresaId = getUsuarioAutenticado().getEmpresaId();
        if (empresaId == null && !isSuperadmin()) {
            throw new IllegalStateException("Usuário não está vinculado a nenhuma empresa");
        }
        return empresaId;
    }
}
