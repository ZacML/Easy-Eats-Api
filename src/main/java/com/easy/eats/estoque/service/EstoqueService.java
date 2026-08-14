package com.easy.eats.estoque.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.easy.eats.estoque.bst.ArvoreInsumos;
import com.easy.eats.estoque.model.Insumo;

@Service
public class EstoqueService {

    private final ArvoreInsumos arvore = new ArvoreInsumos();

    public List<Insumo> listarTodos() {
        return arvore.listarEmOrdem();
    }

    public void cadastrarInsumo(Insumo insumo) {
        arvore.inserir(insumo);
    }

    public Insumo buscarInsumo(String nome) {
        return arvore.buscar(nome);
    }

    public boolean atualizarInsumo(String nome, Insumo novoInsumo) {

        Insumo encontrado = arvore.buscar(nome);

        if (encontrado == null) {
            return false;
        }

        encontrado.setNome(novoInsumo.getNome());
        encontrado.setQuantidade(novoInsumo.getQuantidade());
        encontrado.setUnidade(novoInsumo.getUnidade());

        return true;
    }

    public boolean removerInsumo(String nome) {
        return arvore.remover(nome);
    }
}