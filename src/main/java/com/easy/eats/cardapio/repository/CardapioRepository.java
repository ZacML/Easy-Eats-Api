package com.easy.eats.cardapio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.cardapio.model.Cardapio;

@Repository
public interface CardapioRepository extends JpaRepository<Cardapio,Integer> {

    List<Cardapio> findAllByEmpresaId(Integer empresaId);

    Optional<Cardapio> findByIdAndEmpresaId(Integer id, Integer empresaId);
}
