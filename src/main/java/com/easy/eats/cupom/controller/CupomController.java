package com.easy.eats.cupom.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.easy.eats.cupom.model.Cupom;
import com.easy.eats.cupom.service.CupomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cupom")
public class CupomController {

    private final CupomService service;

    public CupomController(CupomService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cupom> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Cupom buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Cupom salvar(@Valid @RequestBody Cupom cupom) {
        return service.salvar(cupom);
    }

    @PutMapping("/{id}")
    public Cupom atualizar(@PathVariable Integer id, @Valid @RequestBody Cupom cupom) {
        return service.atualizar(id, cupom);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
