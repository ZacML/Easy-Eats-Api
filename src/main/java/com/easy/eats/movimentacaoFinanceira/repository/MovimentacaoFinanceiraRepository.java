package com.easy.eats.movimentacaoFinanceira.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.movimentacaoFinanceira.model.MovimentacaoFinanceira;

@Repository
public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceira, Integer> {

    List<MovimentacaoFinanceira> findAllByEmpresaId(Integer empresaId);

    Optional<MovimentacaoFinanceira> findByIdAndEmpresaId(Integer id, Integer empresaId);

    List<MovimentacaoFinanceira> findAllByCaixaId(Integer caixaId);

    List<MovimentacaoFinanceira> findAllByEmpresaIdAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
            Integer empresaId, LocalDateTime inicio, LocalDateTime fim);
}
