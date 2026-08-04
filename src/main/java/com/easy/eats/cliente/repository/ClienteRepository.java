package com.easy.eats.cliente.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.cliente.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    List<Cliente> findAllByEmpresaId(Integer empresaId);

    Optional<Cliente> findByIdAndEmpresaId(Integer id, Integer empresaId);
}
