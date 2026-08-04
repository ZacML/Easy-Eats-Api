package com.easy.eats.pagamento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.pagamento.model.Pagamento;
import com.easy.eats.pagamento.repository.PagamentoRepository;
import com.easy.eats.security.SecurityUtils;
import com.easy.eats.venda.model.Venda;
import com.easy.eats.venda.repository.VendaRepository;

@Service
public class PagamentoService {

    @Autowired
    PagamentoRepository repository;

    @Autowired
    VendaRepository vendaRepository;

    public Pagamento criar(Pagamento pagamento) {
        pagamento.setId(null);
        pagamento.setVenda(vendaDaMesmaEmpresa(pagamento.getVenda()));
        return repository.save(pagamento);
    }

    public Pagamento salvar(Pagamento pagamento) {
        return repository.save(pagamento);
    }

    public List<Pagamento> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByVenda_Empresa_Id(SecurityUtils.getEmpresaId());
    }

    public Optional<Pagamento> buscarPorId(Integer id) {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findById(id);
        }
        return repository.findByIdAndVenda_Empresa_Id(id, SecurityUtils.getEmpresaId());
    }

    public void deletar(Integer id) {
        if (buscarPorId(id).isEmpty()) {
            return;
        }
        repository.deleteById(id);
    }

    private Venda vendaDaMesmaEmpresa(Venda vendaRecebida) {
        if (vendaRecebida == null || vendaRecebida.getId() == null) {
            throw new IllegalArgumentException("A venda é obrigatória");
        }
        return vendaRepository.findByIdAndEmpresaId(vendaRecebida.getId(), SecurityUtils.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Venda informada não existe ou não pertence à sua empresa"));
    }
}
