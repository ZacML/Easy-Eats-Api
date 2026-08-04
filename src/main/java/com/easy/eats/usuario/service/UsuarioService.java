package com.easy.eats.usuario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.security.AuthenticatedUser;
import com.easy.eats.security.SecurityUtils;
import com.easy.eats.usuario.model.Role;
import com.easy.eats.usuario.model.Usuario;
import com.easy.eats.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private EmpresaRepository empresaRepository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Usuario buscarPorId(Integer id) {
        Usuario usuario = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return usuario;
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória");
        }

        AuthenticatedUser criador = SecurityUtils.getUsuarioAutenticado();

        usuario.setId(null);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (criador.getRole() == Role.SUPERADMIN) {
            if (usuario.getEmpresa() == null || usuario.getEmpresa().getId() == null) {
                throw new IllegalArgumentException("Informe a empresa do usuário");
            }
            usuario.setEmpresa(empresaRepository.getReferenceById(usuario.getEmpresa().getId()));
            usuario.setRole(usuario.getRole() != null ? usuario.getRole() : Role.OPERADOR);
        } else {
            if (usuario.getRole() == Role.SUPERADMIN) {
                throw new IllegalArgumentException("Você não tem permissão para criar um usuário SUPERADMIN");
            }
            usuario.setEmpresa(empresaRepository.getReferenceById(criador.getEmpresaId()));
            usuario.setRole(usuario.getRole() != null ? usuario.getRole() : Role.OPERADOR);
        }

        return repository.save(usuario);
    }

    public Usuario atualizar(Integer id, Usuario usuario) {

        Usuario existente = buscarPorId(id);
        AuthenticatedUser autenticado = SecurityUtils.getUsuarioAutenticado();

        existente.setNome(usuario.getNome());
        existente.setEmail(usuario.getEmail());
        existente.setFlAtivo(usuario.getFlAtivo());

        if (usuario.getSenha() != null && !usuario.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        if (autenticado.getRole() == Role.SUPERADMIN) {
            if (usuario.getRole() != null) {
                existente.setRole(usuario.getRole());
            }
            if (usuario.getEmpresa() != null && usuario.getEmpresa().getId() != null) {
                existente.setEmpresa(empresaRepository.getReferenceById(usuario.getEmpresa().getId()));
            }
        } else if (usuario.getRole() != null && usuario.getRole() != Role.SUPERADMIN) {
            existente.setRole(usuario.getRole());
        }

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
