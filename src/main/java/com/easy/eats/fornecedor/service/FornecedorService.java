package com.easy.eats.fornecedor.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.fornecedor.model.Fornecedor;
import com.easy.eats.fornecedor.repository.FornecedorRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class FornecedorService {

    private final FornecedorRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    public List<Fornecedor> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Fornecedor buscarPorId(Integer id) {
        Fornecedor fornecedor = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (fornecedor == null) {
            throw new RuntimeException("Fornecedor não encontrado");
        }
        return fornecedor;
    }

    public Fornecedor salvar(Fornecedor fornecedor) {
        fornecedor.setId(null);
        fornecedor.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        fornecedor.setDtCriacao(LocalDateTime.now());

        return repository.save(fornecedor);
    }

    public Fornecedor atualizar(Integer id, Fornecedor fornecedor) {

        Fornecedor existente = buscarPorId(id);

        existente.setNome(fornecedor.getNome());
        existente.setCnpj(fornecedor.getCnpj());
        existente.setTelefone(fornecedor.getTelefone());
        existente.setEmail(fornecedor.getEmail());
        existente.setFlAtivo(fornecedor.getFlAtivo());

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
