package com.easy.eats.usuario.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.easy.eats.usuario.model.Usuario;
import com.easy.eats.usuario.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Usuario salvar(@Valid @RequestBody Usuario usuario) {
        return service.salvar(usuario);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Usuario usuario) {

        return service.atualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}