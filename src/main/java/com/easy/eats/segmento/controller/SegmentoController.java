package com.easy.eats.segmento.controller;

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

import com.easy.eats.segmento.model.Funcionalidade;
import com.easy.eats.segmento.model.Segmento;
import com.easy.eats.segmento.service.SegmentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/segmentos")
public class SegmentoController {

    private final SegmentoService service;

    public SegmentoController(SegmentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Segmento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/funcionalidades")
    public ResponseEntity<List<Funcionalidade>> listarFuncionalidades() {
        return ResponseEntity.ok(List.of(Funcionalidade.values()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Segmento> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Segmento> salvar(@Valid @RequestBody Segmento segmento) {
        return ResponseEntity.ok(service.salvar(segmento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Segmento> atualizar(@PathVariable Integer id, @Valid @RequestBody Segmento segmento) {
        return ResponseEntity.ok(service.atualizar(id, segmento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
