package com.easy.eats.composicaoItem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.composicaoItem.model.ComposicaoItem;

@Repository
public interface ComposicaoItemRepository extends JpaRepository<ComposicaoItem, Integer> {

    List<ComposicaoItem> findAllByProdutoId(Integer produtoId);

    Optional<ComposicaoItem> findByIdAndProdutoId(Integer id, Integer produtoId);
}
