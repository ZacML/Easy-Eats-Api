package com.easy.eats.endereco.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cliente.model.Cliente;
import com.easy.eats.cliente.repository.ClienteRepository;
import com.easy.eats.endereco.model.Endereco;
import com.easy.eats.endereco.repository.EnderecoRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository repository;

    @Autowired
    ClienteRepository clienteRepository;

    public Endereco criar(Endereco endereco) {
        endereco.setId(null);
        endereco.setCliente(clienteDaMesmaEmpresa(endereco.getCliente()));
        return repository.save(endereco);
    }

    public Endereco salvar(Endereco endereco) {
        return repository.save(endereco);
    }

    public List<Endereco> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByCliente_Empresa_Id(SecurityUtils.getEmpresaId());
    }

    public Optional<Endereco> buscarPorId(Integer id) {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findById(id);
        }
        return repository.findByIdAndCliente_Empresa_Id(id, SecurityUtils.getEmpresaId());
    }

    public void deletar(Integer id) {
        if (buscarPorId(id).isEmpty()) {
            return;
        }
        repository.deleteById(id);
    }

    private Cliente clienteDaMesmaEmpresa(Cliente clienteRecebido) {
        if (clienteRecebido == null || clienteRecebido.getId() == null) {
            throw new IllegalArgumentException("O cliente é obrigatório");
        }
        return clienteRepository.findByIdAndEmpresaId(clienteRecebido.getId(), SecurityUtils.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente informado não existe ou não pertence à sua empresa"));
    }
}
