package com.easy.eats.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.pedido.model.Pedido;
import com.easy.eats.pedido.repository.PedidoRepository;
import com.easy.eats.venda.model.Venda;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public Pedido salvar(Pedido pedido) {
        return repository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Optional<Pedido> listarArvorePedido(Integer id) {
        return repository.findById(id);
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}