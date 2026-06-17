package com.easy.eats.pedido.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.easy.eats.pedido.model.Pedido;
import com.easy.eats.pedido.service.PedidoService;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping("/criarPedido")
    public ResponseEntity<Pedido> criar(@RequestBody Pedido pedido) {
        Pedido novoPedido = service.salvar(pedido);
        return ResponseEntity.ok(novoPedido);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = service.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizar(@PathVariable Integer id, @RequestBody Pedido pedidoAtualizado) {
        return service.buscarPorId(id).map(pedidoExistente -> {

            pedidoExistente.setNomeProduto(pedidoAtualizado.getNomeProduto());
            pedidoExistente.setQuantidadeProduto(pedidoAtualizado.getQuantidadeProduto());
            ;

            Pedido pedidoSalvo = service.salvar(pedidoExistente);
            return ResponseEntity.ok(pedidoSalvo);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable Integer id) {
        return service.listarArvorePedido(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (service.buscarPorId(id).isPresent()) {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}