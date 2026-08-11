package com.easy.eats.itemVenda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.adicional.model.Adicional;
import com.easy.eats.adicional.repository.AdicionalRepository;
import com.easy.eats.composicaoItem.model.ComposicaoItem;
import com.easy.eats.composicaoItem.repository.ComposicaoItemRepository;
import com.easy.eats.itemVenda.model.ItemVenda;
import com.easy.eats.itemVenda.model.ItemVendaAdicional;
import com.easy.eats.itemVenda.model.ItemVendaComposicaoRemovida;
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

    @Autowired
    ComposicaoItemRepository composicaoItemRepository;

    @Autowired
    AdicionalRepository adicionalRepository;

    public ItemVenda criar(ItemVenda itemVenda) {
        Integer empresaId = SecurityUtils.getEmpresaId();

        itemVenda.setId(null);
        itemVenda.setVenda(vendaDaMesmaEmpresa(itemVenda.getVenda(), empresaId));
        Produto produto = produtoDaMesmaEmpresa(itemVenda.getProduto(), empresaId);
        itemVenda.setProduto(produto);

        itemVenda.setComposicaoRemovida(
                resolverComposicaoRemovida(itemVenda.getComposicaoRemovida(), produto, itemVenda));
        itemVenda.setAdicionais(resolverAdicionais(itemVenda.getAdicionais(), produto, itemVenda));

        return repository.save(itemVenda);
    }

    private List<ItemVendaComposicaoRemovida> resolverComposicaoRemovida(
            List<ItemVendaComposicaoRemovida> recebidos, Produto produto, ItemVenda itemVenda) {

        List<ItemVendaComposicaoRemovida> resolvidos = new ArrayList<>();
        if (recebidos == null) {
            return resolvidos;
        }

        for (ItemVendaComposicaoRemovida recebido : recebidos) {
            if (recebido.getComposicaoItem() == null || recebido.getComposicaoItem().getId() == null) {
                throw new IllegalArgumentException("O item de composição é obrigatório");
            }

            ComposicaoItem item = composicaoItemRepository
                    .findByIdAndProdutoId(recebido.getComposicaoItem().getId(), produto.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Item de composição informado não pertence ao produto"));

            ItemVendaComposicaoRemovida resolvido = new ItemVendaComposicaoRemovida();
            resolvido.setItemVenda(itemVenda);
            resolvido.setComposicaoItem(item);
            resolvidos.add(resolvido);
        }
        return resolvidos;
    }

    private List<ItemVendaAdicional> resolverAdicionais(
            List<ItemVendaAdicional> recebidos, Produto produto, ItemVenda itemVenda) {

        List<ItemVendaAdicional> resolvidos = new ArrayList<>();
        if (recebidos == null) {
            return resolvidos;
        }

        for (ItemVendaAdicional recebido : recebidos) {
            if (recebido.getAdicional() == null || recebido.getAdicional().getId() == null) {
                throw new IllegalArgumentException("O adicional é obrigatório");
            }

            Adicional adicional = adicionalRepository
                    .findByIdAndProdutoId(recebido.getAdicional().getId(), produto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Adicional informado não pertence ao produto"));

            ItemVendaAdicional resolvido = new ItemVendaAdicional();
            resolvido.setItemVenda(itemVenda);
            resolvido.setAdicional(adicional);
            resolvido.setQuantidade(recebido.getQuantidade() != null ? recebido.getQuantidade() : 1);
            resolvido.setNome(adicional.getNome());
            resolvido.setPreco(adicional.getPreco());
            resolvidos.add(resolvido);
        }
        return resolvidos;
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
