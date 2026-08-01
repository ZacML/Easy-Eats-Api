package com.easy.eats.categoria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.categoria.model.Categoria;
import com.easy.eats.categoria.repository.CategoriaRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.security.SecurityUtils;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Categoria buscarPorId(Integer id) {
        Categoria categoria = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (categoria == null) {
            throw new RuntimeException("Categoria não encontrada");
        }
        return categoria;
    }

    public Categoria salvar(Categoria categoria) {
        categoria.setId(null);
        categoria.setEmpresa(empresaRepository.getReferenceById(SecurityUtils.getEmpresaId()));
        return repository.save(categoria);
    }

    public Categoria atualizar(Integer id, Categoria categoria) {

        Categoria categoriaExistente = buscarPorId(id);

        categoriaExistente.setNome(categoria.getNome());
        categoriaExistente.setDescricao(categoria.getDescricao());
        categoriaExistente.setFlativo(categoria.getFlativo());
        categoriaExistente.setDt_alteracao(categoria.getDt_alteracao());

        return repository.save(categoriaExistente);
    }

    public void deletar(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }
}
