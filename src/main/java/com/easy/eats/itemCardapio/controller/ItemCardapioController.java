package com.easy.eats.itemCardapio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.easy.eats.itemCardapio.model.ItemCardapio;
import com.easy.eats.itemCardapio.service.ItemCardapioService;

@RestController
public class ItemCardapioController {

    @Autowired
    ItemCardapioService service;

    @GetMapping("/cardapio/{cardapioId}/itens")
    public List<ItemCardapio> listar(@PathVariable Integer cardapioId) {
        return service.listar(cardapioId);
    }

    @PostMapping("/cardapio/{cardapioId}/itens")
    public ResponseEntity<ItemCardapio> adicionar(@PathVariable Integer cardapioId, @RequestBody ItemCardapio item) {
        return ResponseEntity.ok(service.adicionar(cardapioId, item));
    }

    @PutMapping("/item-cardapio/{id}")
    public ResponseEntity<ItemCardapio> atualizar(@PathVariable Integer id, @RequestBody ItemCardapio item) {
        return ResponseEntity.ok(service.atualizar(id, item));
    }

    @DeleteMapping("/item-cardapio/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
