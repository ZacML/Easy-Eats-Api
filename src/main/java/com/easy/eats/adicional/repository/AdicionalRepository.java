package com.easy.eats.adicional.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.adicional.model.Adicional;

@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Integer> {

    List<Adicional> findAllByProdutoId(Integer produtoId);

    Optional<Adicional> findByIdAndProdutoId(Integer id, Integer produtoId);
}
