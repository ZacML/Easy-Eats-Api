package com.easy.eats.itemCardapio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.itemCardapio.model.ItemCardapio;

@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Integer> {

    List<ItemCardapio> findAllByCardapioIdOrderByOrdemAsc(Integer cardapioId);

    Optional<ItemCardapio> findByIdAndCardapio_Empresa_Id(Integer id, Integer empresaId);

    boolean existsByCardapioIdAndProdutoId(Integer cardapioId, Integer produtoId);
}
