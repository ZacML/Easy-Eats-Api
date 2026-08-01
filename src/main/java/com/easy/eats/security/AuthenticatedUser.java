package com.easy.eats.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.easy.eats.usuario.model.Role;
import com.easy.eats.usuario.model.Usuario;

public class AuthenticatedUser implements UserDetails {

    private final Integer usuarioId;
    private final String email;
    private final String senha;
    private final Integer empresaId;
    private final Role role;

    public AuthenticatedUser(Usuario usuario) {
        this.usuarioId = usuario.getId();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.empresaId = usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null;
        this.role = usuario.getRole() != null ? usuario.getRole() : Role.OPERADOR;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public Integer getEmpresaId() {
        return empresaId;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
