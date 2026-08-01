package com.easy.eats.empresa.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easy.eats.empresa.model.model.Empresa;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.segmento.model.Funcionalidade;
import com.easy.eats.segmento.model.Segmento;
import com.easy.eats.segmento.repository.SegmentoRepository;

@Service
public class EmpresaService {
    private final EmpresaRepository repository;
    private final SegmentoRepository segmentoRepository;

    public EmpresaService(EmpresaRepository repository, SegmentoRepository segmentoRepository) {
        this.repository = repository;
        this.segmentoRepository = segmentoRepository;
    }

    public List<Empresa> listarTodos() {
        return repository.findAll();
    }

    public Empresa buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    public Empresa salvar(Empresa empresa) {
        empresa.setId(null);
        empresa.setSegmento(segmentoDoIdRecebido(empresa.getSegmento()));
        return repository.save(empresa);
    }

    public Empresa atualizar(Integer id, Empresa empresa) {

        Empresa empresaExistente = buscarPorId(id);

        empresaExistente.setNome(empresa.getNome());
        empresaExistente.setCnpj(empresa.getCnpj());
        empresaExistente.setEmail(empresa.getEmail());
        empresaExistente.setTelefone(empresa.getTelefone());
        empresaExistente.setFlAtivo(empresa.getFlAtivo());
        empresaExistente.setDtCriacao(empresa.getDtCriacao());
        empresaExistente.setDtAlteracao(empresa.getDtAlteracao());
        empresaExistente.setSegmento(segmentoDoIdRecebido(empresa.getSegmento()));

        return repository.save(empresaExistente);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    /**
     * Funcionalidades liberadas para a empresa, de acordo com o segmento de
     * negócio vinculado a ela. Sem segmento definido, libera tudo (evita
     * quebrar empresas cadastradas antes dessa configuração existir).
     */
    public List<Funcionalidade> funcionalidadesHabilitadas(Empresa empresa) {
        if (empresa == null || empresa.getSegmento() == null || empresa.getSegmento().getFuncionalidades() == null
                || empresa.getSegmento().getFuncionalidades().isEmpty()) {
            return Arrays.asList(Funcionalidade.values());
        }
        return empresa.getSegmento().getFuncionalidades().stream().toList();
    }

    private Segmento segmentoDoIdRecebido(Segmento segmentoRecebido) {
        if (segmentoRecebido == null || segmentoRecebido.getId() == null) {
            return null;
        }
        return segmentoRepository.findById(segmentoRecebido.getId())
                .orElseThrow(() -> new IllegalArgumentException("Segmento informado não existe"));
    }
}
