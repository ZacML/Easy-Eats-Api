package com.easy.eats.cashback.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easy.eats.cashback.model.CashbackConfig;

@Repository
public interface CashbackConfigRepository extends JpaRepository<CashbackConfig, Integer> {

    Optional<CashbackConfig> findByEmpresaId(Integer empresaId);
}
