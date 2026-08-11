package com.easy.eats.comanda.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easy.eats.comanda.model.Comanda;
import com.easy.eats.comanda.model.ItensComandaRequest;
import com.easy.eats.comanda.service.ComandaService;
import com.easy.eats.venda.model.Venda;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/comanda")
public class ComandaController {

    @Autowired
    ComandaService service;

    @PostMapping("/abrir")
    public ResponseEntity<Comanda> abrir(@RequestBody Comanda dados) {
        return ResponseEntity.ok(service.abrir(dados));
    }

    @GetMapping
    public List<Comanda> listar(@RequestParam(required = false) String status) {
        return service.listar(status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comanda> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<Venda> adicionarItens(@PathVariable Integer id,
            @Valid @RequestBody ItensComandaRequest request) {
        return ResponseEntity.ok(service.adicionarItens(id, request));
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<Comanda> fechar(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(service.fechar(id, body.get("metodoPagamento")));
    }
}
