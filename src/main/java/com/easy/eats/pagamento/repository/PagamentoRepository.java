package com.easy.eats.pagamento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.pagamento.model.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    List<Pagamento> findAllByVenda_Empresa_Id(Integer empresaId);

    Optional<Pagamento> findByIdAndVenda_Empresa_Id(Integer id, Integer empresaId);

    List<Pagamento> findAllByCaixaId(Integer caixaId);
}
