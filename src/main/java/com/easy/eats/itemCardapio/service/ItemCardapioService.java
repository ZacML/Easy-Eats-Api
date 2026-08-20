package com.easy.eats.itemCardapio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cardapio.model.Cardapio;
import com.easy.eats.cardapio.service.CardapioService;
import com.easy.eats.itemCardapio.model.ItemCardapio;
import com.easy.eats.itemCardapio.repository.ItemCardapioRepository;
import com.easy.eats.produto.model.Produto;
import com.easy.eats.produto.repository.ProdutoRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class ItemCardapioService {

    @Autowired
    ItemCardapioRepository repository;

    @Autowired
    CardapioService cardapioService;

    @Autowired
    ProdutoRepository produtoRepository;

    public List<ItemCardapio> listar(Integer cardapioId) {
        // buscarPorId já garante que o cardápio pertence à empresa do usuário.
        cardapioService.buscarPorId(cardapioId);
        return repository.findAllByCardapioIdOrderByOrdemAsc(cardapioId);
    }

    public ItemCardapio adicionar(Integer cardapioId, ItemCardapio dados) {
        Cardapio cardapio = cardapioService.buscarPorId(cardapioId);
        Integer empresaId = SecurityUtils.getEmpresaId();

        if (dados.getProduto() == null || dados.getProduto().getId() == null) {
            throw new IllegalArgumentException("O produto é obrigatório");
        }
        Produto produto = produtoRepository.findByIdAndEmpresaId(dados.getProduto().getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Produto informado não existe ou não pertence à sua empresa"));

        if (repository.existsByCardapioIdAndProdutoId(cardapioId, produto.getId())) {
            throw new IllegalArgumentException("Esse produto já está nesse cardápio");
        }

        ItemCardapio item = new ItemCardapio();
        item.setCardapio(cardapio);
        item.setProduto(produto);
        item.setOrdem(dados.getOrdem() != null ? dados.getOrdem() : 0);
        item.setDisponivel(dados.getDisponivel() != null ? dados.getDisponivel() : true);
        item.setPrecoOverride(dados.getPrecoOverride());
        item.setDescricaoOverride(dados.getDescricaoOverride());
        item.setFotoUrl(dados.getFotoUrl());

        return repository.save(item);
    }

    public ItemCardapio atualizar(Integer id, ItemCardapio dados) {
        ItemCardapio existente = buscarPorId(id);

        existente.setOrdem(dados.getOrdem());
        existente.setDisponivel(dados.getDisponivel());
        existente.setPrecoOverride(dados.getPrecoOverride());
        existente.setDescricaoOverride(dados.getDescricaoOverride());
        existente.setFotoUrl(dados.getFotoUrl());

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    private ItemCardapio buscarPorId(Integer id) {
        ItemCardapio item = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndCardapio_Empresa_Id(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (item == null) {
            throw new RuntimeException("Item de cardápio não encontrado");
        }
        return item;
    }
}
