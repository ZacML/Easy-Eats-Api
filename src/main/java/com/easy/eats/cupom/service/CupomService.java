package com.easy.eats.cupom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cupom.model.Cupom;
import com.easy.eats.cupom.repository.CupomRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class CupomService {

    private final CupomRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public CupomService(CupomRepository repository) {
        this.repository = repository;
    }

    public List<Cupom> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Cupom buscarPorId(Integer id) {
        Cupom cupom = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (cupom == null) {
            throw new RuntimeException("Cupom não encontrado");
        }
        return cupom;
    }

    public Cupom salvar(Cupom cupom) {
        cupom.setId(null);
        cupom.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        if (cupom.getFlAtivo() == null) {
            cupom.setFlAtivo(true);
        }
        return repository.save(cupom);
    }

    public Cupom atualizar(Integer id, Cupom cupom) {
        Cupom existente = buscarPorId(id);

        existente.setCodigo(cupom.getCodigo());
        existente.setTipoDesconto(cupom.getTipoDesconto());
        existente.setValorDesconto(cupom.getValorDesconto());
        existente.setDtValidadeInicio(cupom.getDtValidadeInicio());
        existente.setDtValidadeFim(cupom.getDtValidadeFim());
        existente.setLimiteUsoTotal(cupom.getLimiteUsoTotal());
        existente.setLimiteUsoPorCliente(cupom.getLimiteUsoPorCliente());
        existente.setValorMinimoPedido(cupom.getValorMinimoPedido());
        existente.setFlAtivo(cupom.getFlAtivo());

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
