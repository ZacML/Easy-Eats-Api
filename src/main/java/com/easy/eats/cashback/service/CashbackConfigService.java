package com.easy.eats.cashback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cashback.model.CashbackConfig;
import com.easy.eats.cashback.repository.CashbackConfigRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class CashbackConfigService {

    @Autowired
    CashbackConfigRepository repository;

    @Autowired
    EmpresaRepository empresaRepository;

    public CashbackConfig buscarOuCriar() {
        Integer empresaId = SecurityUtils.getEmpresaId();
        return repository.findByEmpresaId(empresaId).orElseGet(() -> {
            CashbackConfig config = new CashbackConfig();
            config.setPercentualAcumulo(0.0);
            config.setFlAtivo(false);
            config.setEmpresa(empresaRepository.getReferenceById(empresaId));
            return repository.save(config);
        });
    }

    public CashbackConfig atualizar(CashbackConfig dados) {
        CashbackConfig existente = buscarOuCriar();

        existente.setPercentualAcumulo(dados.getPercentualAcumulo());
        existente.setValorMinimoParaAcumular(dados.getValorMinimoParaAcumular());
        existente.setFlAtivo(dados.getFlAtivo());

        return repository.save(existente);
    }
}
