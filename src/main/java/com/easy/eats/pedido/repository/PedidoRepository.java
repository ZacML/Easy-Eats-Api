package com.easy.eats.pedido.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easy.eats.pedido.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findAllByEmpresaId(Integer empresaId);

    Optional<Pedido> findByIdAndEmpresaId(Integer id, Integer empresaId);

    @Query("SELECT p FROM Pedido p WHERE p.empresa.id = :empresaId AND p.status IN ('AGUARDANDO', 'PREPARANDO') ORDER BY p.dataCriacao ASC")
    List<Pedido> findFilaAtivaPorEmpresa(@Param("empresaId") Integer empresaId);
}
