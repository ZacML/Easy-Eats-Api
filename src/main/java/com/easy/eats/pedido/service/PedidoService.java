package com.easy.eats.pedido.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.pedido.bst.ArvorePedido;
import com.easy.eats.pedido.model.Pedido;
import com.easy.eats.pedido.repository.PedidoRepository;
import com.easy.eats.security.SecurityUtils;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public Pedido criar(Pedido pedido) {
        pedido.setId(null);
        pedido.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        return repository.save(pedido);
    }

    public Pedido salvar(Pedido pedido) {
        return repository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Optional<Pedido> listarArvorePedido(Integer id) {
        List<Pedido> todosPedidos = listarTodos();

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
        if (SecurityUtils.isSuperadmin()) {
            return repository.findById(id);
        }
        return repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId());
    }

    public void deletar(Integer id) {
        if (buscarPorId(id).isEmpty()) {
            return;
        }
        repository.deleteById(id);
    }
}
