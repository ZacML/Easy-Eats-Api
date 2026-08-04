package com.easy.eats.segmento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easy.eats.segmento.model.Segmento;
import com.easy.eats.segmento.repository.SegmentoRepository;

@Service
public class SegmentoService {

    private final SegmentoRepository repository;

    public SegmentoService(SegmentoRepository repository) {
        this.repository = repository;
    }

    public List<Segmento> listarTodos() {
        return repository.findAll();
    }

    public Segmento buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));
    }

    public Segmento salvar(Segmento segmento) {
        segmento.setId(null);
        if (segmento.getFlAtivo() == null) {
            segmento.setFlAtivo(true);
        }
        return repository.save(segmento);
    }

    public Segmento atualizar(Integer id, Segmento segmento) {
        Segmento existente = buscarPorId(id);

        existente.setNome(segmento.getNome());
        existente.setDescricao(segmento.getDescricao());
        existente.setFlAtivo(segmento.getFlAtivo());
        existente.setFuncionalidades(segmento.getFuncionalidades());

        return repository.save(existente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
