package com.easy.eats.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.pedido.bst.ArvorePedido;
import com.easy.eats.pedido.model.Pedido;
import com.easy.eats.pedido.repository.PedidoRepository;
import com.easy.eats.venda.model.Venda;
import com.easy.eats.pedido.enums.StatusPedido;

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
        List<Pedido> todosPedidos = repository.findAll();

        if (todosPedidos.isEmpty()) {
            return Optional.empty();
        }

        ArvorePedido arvore = new ArvorePedido();

        for (Pedido p : todosPedidos) {
            arvore.inserir(p);
        }

        Pedido encontrado = arvore.buscar(id);

        return Optional.ofNullable(encontrado);
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public List<Pedido> obterFilaDePedidos() {
        return repository.findFilaAtiva();
    }

    public Pedido iniciarPreparo(Integer id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        if (pedido.getStatus() == StatusPedido.AGUARDANDO) {
            pedido.setStatus(StatusPedido.PREPARANDO);
            return repository.save(pedido);
        }
        
        return pedido;
    }

    public Pedido marcarComoPronto(Integer id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        
        pedido.setStatus(StatusPedido.PRONTO);
        return repository.save(pedido);
    }
}
