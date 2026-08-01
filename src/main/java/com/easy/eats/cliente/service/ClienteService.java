package com.easy.eats.cliente.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cliente.model.Cliente;
import com.easy.eats.cliente.repository.ClienteRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;

    @Autowired
    EmpresaRepository empresaRepository;

    public Cliente criar(Cliente cliente) {
        cliente.setId(null);
        cliente.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        return repository.save(cliente);
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Optional<Cliente> buscarPorId(Integer id) {
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
