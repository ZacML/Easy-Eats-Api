package com.easy.eats.composicaoItem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.composicaoItem.model.ComposicaoItem;
import com.easy.eats.composicaoItem.repository.ComposicaoItemRepository;
import com.easy.eats.produto.model.Produto;
import com.easy.eats.produto.service.ProdutoService;

@Service
public class ComposicaoItemService {

    private final ComposicaoItemRepository repository;

    @Autowired
    private ProdutoService produtoService;

    public ComposicaoItemService(ComposicaoItemRepository repository) {
        this.repository = repository;
    }

    public List<ComposicaoItem> listarPorProduto(Integer produtoId) {
        produtoService.buscarPorId(produtoId);
        return repository.findAllByProdutoId(produtoId);
    }

    public ComposicaoItem salvar(Integer produtoId, ComposicaoItem item) {
        Produto produto = produtoService.buscarPorId(produtoId);

        item.setId(null);
        item.setProduto(produto);
        if (item.getRemovivel() == null) {
            item.setRemovivel(true);
        }
        return repository.save(item);
    }

    public ComposicaoItem atualizar(Integer produtoId, Integer id, ComposicaoItem item) {
        ComposicaoItem existente = buscarPorIdEProduto(produtoId, id);

        existente.setNome(item.getNome());
        existente.setRemovivel(item.getRemovivel() != null ? item.getRemovivel() : existente.getRemovivel());

        return repository.save(existente);
    }

    public void deletar(Integer produtoId, Integer id) {
        buscarPorIdEProduto(produtoId, id);
        repository.deleteById(id);
    }

    private ComposicaoItem buscarPorIdEProduto(Integer produtoId, Integer id) {
        produtoService.buscarPorId(produtoId);
        return repository.findByIdAndProdutoId(id, produtoId)
                .orElseThrow(() -> new RuntimeException("Item de composição não encontrado"));
    }
}
