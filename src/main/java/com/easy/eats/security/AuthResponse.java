package com.easy.eats.security;

import java.util.List;

public class AuthResponse {

    private String token;
    private Integer usuarioId;
    private String nome;
    private String email;
    private String role;
    private List<String> funcionalidades;

    public AuthResponse(String token, Integer usuarioId, String nome, String email, String role,
            List<String> funcionalidades) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.funcionalidades = funcionalidades;
    }

    public String getToken() {
        return token;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public List<String> getFuncionalidades() {
        return funcionalidades;
    }
}
