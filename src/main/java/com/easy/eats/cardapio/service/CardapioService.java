package com.easy.eats.cardapio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cardapio.model.Cardapio;
import com.easy.eats.cardapio.repository.CardapioRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class CardapioService {

    private final CardapioRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public CardapioService(CardapioRepository repository) {
        this.repository = repository;
    }

    public List<Cardapio> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Cardapio buscarPorId(Integer id) {
        Cardapio cardapio = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (cardapio == null) {
            throw new RuntimeException("Cardápio não encontrado");
        }
        return cardapio;
    }

    public Cardapio salvar(Cardapio cardapio) {
        cardapio.setId(null);
        cardapio.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        return repository.save(cardapio);
    }

    public Cardapio atualizar(Integer id, Cardapio cardapio) {

        Cardapio cardapioExistente = buscarPorId(id);

        cardapioExistente.setNome(cardapio.getNome());
        cardapioExistente.setDescricao(cardapio.getDescricao());
        cardapioExistente.setFlAtivo(cardapio.getFlAtivo());
        cardapioExistente.setDtAlteracao(cardapio.getDtAlteracao());

        return repository.save(cardapioExistente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    public Cardapio marcarPadrao(Integer id) {
        Cardapio cardapio = buscarPorId(id);
        Integer empresaId = cardapio.getEmpresa().getId();

        for (Cardapio outro : repository.findAllByEmpresaId(empresaId)) {
            if (Boolean.TRUE.equals(outro.getPadrao()) && !outro.getId().equals(id)) {
                outro.setPadrao(false);
                repository.save(outro);
            }
        }

        cardapio.setPadrao(true);
        return repository.save(cardapio);
    }
}
