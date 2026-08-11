package com.easy.eats.composicaoItem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easy.eats.composicaoItem.model.ComposicaoItem;
import com.easy.eats.composicaoItem.service.ComposicaoItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos/{produtoId}/composicao")
public class ComposicaoItemController {

    private final ComposicaoItemService service;

    public ComposicaoItemController(ComposicaoItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComposicaoItem> listar(@PathVariable Integer produtoId) {
        return service.listarPorProduto(produtoId);
    }

    @PostMapping
    public ResponseEntity<ComposicaoItem> salvar(
            @PathVariable Integer produtoId,
            @Valid @RequestBody ComposicaoItem item) {
        return ResponseEntity.ok(service.salvar(produtoId, item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComposicaoItem> atualizar(
            @PathVariable Integer produtoId,
            @PathVariable Integer id,
            @Valid @RequestBody ComposicaoItem item) {
        return ResponseEntity.ok(service.atualizar(produtoId, id, item));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer produtoId, @PathVariable Integer id) {
        service.deletar(produtoId, id);
        return ResponseEntity.noContent().build();
    }
}
