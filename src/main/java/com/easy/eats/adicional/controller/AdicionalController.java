package com.easy.eats.adicional.controller;

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

import com.easy.eats.adicional.model.Adicional;
import com.easy.eats.adicional.service.AdicionalService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos/{produtoId}/adicionais")
public class AdicionalController {

    private final AdicionalService service;

    public AdicionalController(AdicionalService service) {
        this.service = service;
    }

    @GetMapping
    public List<Adicional> listar(@PathVariable Integer produtoId) {
        return service.listarPorProduto(produtoId);
    }

    @PostMapping
    public ResponseEntity<Adicional> salvar(
            @PathVariable Integer produtoId,
            @Valid @RequestBody Adicional adicional) {
        return ResponseEntity.ok(service.salvar(produtoId, adicional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adicional> atualizar(
            @PathVariable Integer produtoId,
            @PathVariable Integer id,
            @Valid @RequestBody Adicional adicional) {
        return ResponseEntity.ok(service.atualizar(produtoId, id, adicional));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer produtoId, @PathVariable Integer id) {
        service.deletar(produtoId, id);
        return ResponseEntity.noContent().build();
    }
}
