package com.easy.eats.pedido.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.easy.eats.pedido.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p FROM Pedido p WHERE p.status IN ('AGUARDANDO', 'PREPARANDO') ORDER BY p.dataCriacao ASC")
    List<Pedido> findFilaAtiva();

}
