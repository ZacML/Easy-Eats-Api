package com.easy.eats.usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAllByEmpresaId(Integer empresaId);

    Optional<Usuario> findByIdAndEmpresaId(Integer id, Integer empresaId);
}