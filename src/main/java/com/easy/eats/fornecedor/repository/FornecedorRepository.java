package com.easy.eats.fornecedor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.fornecedor.model.Fornecedor;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

    List<Fornecedor> findAllByEmpresaId(Integer empresaId);

    Optional<Fornecedor> findByIdAndEmpresaId(Integer id, Integer empresaId);
}