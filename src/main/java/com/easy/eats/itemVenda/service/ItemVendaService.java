package com.easy.eats.itemVenda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.itemVenda.model.ItemVenda;
import com.easy.eats.itemVenda.repository.ItemVendaRepository;
import com.easy.eats.produto.model.Produto;
import com.easy.eats.produto.repository.ProdutoRepository;
import com.easy.eats.security.SecurityUtils;
import com.easy.eats.venda.model.Venda;
import com.easy.eats.venda.repository.VendaRepository;

@Service
public class ItemVendaService {

    @Autowired
    ItemVendaRepository repository;

    @Autowired
    VendaRepository vendaRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    public ItemVenda criar(ItemVenda itemVenda) {
        Integer empresaId = SecurityUtils.getEmpresaId();

        itemVenda.setId(null);
        itemVenda.setVenda(vendaDaMesmaEmpresa(itemVenda.getVenda(), empresaId));
        itemVenda.setProduto(produtoDaMesmaEmpresa(itemVenda.getProduto(), empresaId));

        return repository.save(itemVenda);
    }

    public ItemVenda salvar(ItemVenda venda) {
        return repository.save(venda);
    }

    public List<ItemVenda> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByVenda_Empresa_Id(SecurityUtils.getEmpresaId());
    }

    public Optional<ItemVenda> buscarPorId(Integer id) {
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

    private Venda vendaDaMesmaEmpresa(Venda vendaRecebida, Integer empresaId) {
        if (vendaRecebida == null || vendaRecebida.getId() == null) {
            throw new IllegalArgumentException("A venda é obrigatória");
        }
        return vendaRepository.findByIdAndEmpresaId(vendaRecebida.getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Venda informada não existe ou não pertence à sua empresa"));
    }

    private Produto produtoDaMesmaEmpresa(Produto produtoRecebido, Integer empresaId) {
        if (produtoRecebido == null || produtoRecebido.getId() == null) {
            throw new IllegalArgumentException("O produto é obrigatório");
        }
        return produtoRepository.findByIdAndEmpresaId(produtoRecebido.getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Produto informado não existe ou não pertence à sua empresa"));
    }
}
