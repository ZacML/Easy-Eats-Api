package com.easy.eats.categoria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.categoria.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findAllByEmpresaId(Integer empresaId);

    Optional<Categoria> findByIdAndEmpresaId(Integer id, Integer empresaId);
}
