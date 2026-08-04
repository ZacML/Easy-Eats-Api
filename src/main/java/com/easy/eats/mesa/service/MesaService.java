package com.easy.eats.mesa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.mesa.model.Mesa;
import com.easy.eats.mesa.repository.MesaRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class MesaService {

    @Autowired
    MesaRepository repository;

    @Autowired
    EmpresaRepository empresaRepository;

    public Mesa criar(Mesa mesa) {
        mesa.setId(null);
        mesa.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        return repository.save(mesa);
    }

    public Mesa salvar(Mesa mesa) {
        return repository.save(mesa);
    }

    public List<Mesa> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Optional<Mesa> buscarPorId(Integer id) {
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
