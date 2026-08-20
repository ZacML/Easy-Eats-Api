package com.easy.eats.adicional.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.adicional.model.Adicional;
import com.easy.eats.adicional.repository.AdicionalRepository;
import com.easy.eats.produto.model.Produto;
import com.easy.eats.produto.service.ProdutoService;

@Service
public class AdicionalService {

    private final AdicionalRepository repository;

    @Autowired
    private ProdutoService produtoService;

    public AdicionalService(AdicionalRepository repository) {
        this.repository = repository;
    }

    public List<Adicional> listarPorProduto(Integer produtoId) {
        produtoService.buscarPorId(produtoId);
        return repository.findAllByProdutoId(produtoId);
    }

    public Adicional salvar(Integer produtoId, Adicional adicional) {
        Produto produto = produtoService.buscarPorId(produtoId);

        adicional.setId(null);
        adicional.setProduto(produto);
        return repository.save(adicional);
    }

    public Adicional atualizar(Integer produtoId, Integer id, Adicional adicional) {
        Adicional existente = buscarPorIdEProduto(produtoId, id);

        existente.setNome(adicional.getNome());
        existente.setPreco(adicional.getPreco());

        return repository.save(existente);
    }

    public void deletar(Integer produtoId, Integer id) {
        buscarPorIdEProduto(produtoId, id);
        repository.deleteById(id);
    }

    private Adicional buscarPorIdEProduto(Integer produtoId, Integer id) {
        produtoService.buscarPorId(produtoId);
        return repository.findByIdAndProdutoId(id, produtoId)
                .orElseThrow(() -> new RuntimeException("Adicional não encontrado"));
    }
}
