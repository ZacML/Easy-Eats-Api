package com.easy.eats.estoque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import com.easy.eats.estoque.model.Insumo;
import com.easy.eats.estoque.service.EstoqueService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping("/insumo")
    public String cadastrar(@RequestBody Insumo insumo) {
        estoqueService.cadastrarInsumo(insumo);
        return "Insumo cadastrado com sucesso!";
    }

    @GetMapping("/buscar/{nome}")
    public Insumo buscar(@PathVariable String nome) {
        return estoqueService.buscarInsumo(nome);
    }

  @PutMapping("/atualizar/{nome}")
public String atualizar(
        @PathVariable String nome,
        @RequestBody Insumo insumo) {

    boolean atualizado = estoqueService.atualizarInsumo(nome, insumo);

    if (atualizado) {
        return "Insumo atualizado com sucesso!";
    } else {
        return "Insumo não encontrado!";
    }
}

@DeleteMapping("/remover/{nome}")
public String remover(@PathVariable String nome) {

    boolean removido = estoqueService.removerInsumo(nome);

    if (removido) {
        return "Insumo removido com sucesso!";
    } else {
        return "Insumo não encontrado!";
    }
}
}