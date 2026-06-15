package com.easy.eats.venda.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.venda.model.Venda;
import com.easy.eats.venda.repository.VendaRepository;

@Service
public class VendaService {

    @Autowired
    VendaRepository repository;

    public Venda salvar(Venda venda) {
        return repository.save(venda);
    }

    public List<Venda> listarTodos() {
        return repository.findAll();
    }

    public Optional<Venda> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }
}
