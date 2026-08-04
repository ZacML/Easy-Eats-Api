package com.easy.eats.venda.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.itemVenda.model.ItemVenda;
import com.easy.eats.itemVenda.service.ItemVendaService;
import com.easy.eats.mesa.model.Mesa;
import com.easy.eats.mesa.repository.MesaRepository;
import com.easy.eats.security.SecurityUtils;
import com.easy.eats.usuario.model.Usuario;
import com.easy.eats.usuario.repository.UsuarioRepository;
import com.easy.eats.venda.model.Venda;
import com.easy.eats.venda.quicksort.ProdutoRanking;
import com.easy.eats.venda.quicksort.QuickSortProdutos;
import com.easy.eats.venda.repository.VendaRepository;

@Service
public class VendaService {

    @Autowired
    VendaRepository repository;

    @Autowired
    ItemVendaService itemVendaService;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    MesaRepository mesaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public Venda criar(Venda venda) {
        Integer empresaId = SecurityUtils.getEmpresaId();

        boolean temMesa = venda.getMesa() != null && venda.getMesa().getId() != null;
        boolean temNomeCliente = venda.getNomeCliente() != null && !venda.getNomeCliente().isBlank();

        if (!temMesa && !temNomeCliente) {
            throw new IllegalArgumentException("Informe a mesa ou o nome do cliente");
        }

        venda.setId(null);
        venda.setEmpresa(empresaRepository.getReferenceById(empresaId));
        venda.setMesa(temMesa ? mesaDaMesmaEmpresa(venda.getMesa(), empresaId) : null);
        venda.setUsuario(usuarioDaMesmaEmpresa(venda.getUsuario(), empresaId));

        return repository.save(venda);
    }

    public Venda salvar(Venda venda) {
        return repository.save(venda);
    }

    public List<Venda> listarTodos() {
        if (SecurityUtils.isSuperadmin()) {
            return repository.findAll();
        }
        return repository.findAllByEmpresaId(SecurityUtils.getEmpresaId());
    }

    public Optional<Venda> buscarPorId(Integer id) {
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

    public List<ProdutoRanking> rankingProdutos() {
        List<ItemVenda> itens = itemVendaService.listarTodos();

        Map<Integer, ProdutoRanking> mapa = new HashMap<>();

        for (ItemVenda item : itens) {
            if (item.getProduto() == null) continue;

            Integer idProduto = item.getProduto().getId();
            String nome = item.getProduto().getNome();
            Double quantidade = item.getQuantidade() != null ? item.getQuantidade() : 0.0;
            Double valorTotal = item.getValor_total() != null ? item.getValor_total() : 0.0;

            if (mapa.containsKey(idProduto)) {
                ProdutoRanking ranking = mapa.get(idProduto);
                ranking.setQuantidadeVendida(ranking.getQuantidadeVendida() + quantidade);
                ranking.setFaturamentoTotal(ranking.getFaturamentoTotal() + valorTotal);
            } else {
                mapa.put(idProduto, new ProdutoRanking(nome, quantidade, valorTotal));
            }
        }

        List<ProdutoRanking> lista = new ArrayList<>(mapa.values());
        new QuickSortProdutos().ordenar(lista);
        return lista;
    }

    private Mesa mesaDaMesmaEmpresa(Mesa mesaRecebida, Integer empresaId) {
        if (mesaRecebida == null || mesaRecebida.getId() == null) {
            throw new IllegalArgumentException("A mesa é obrigatória");
        }
        return mesaRepository.findByIdAndEmpresaId(mesaRecebida.getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Mesa informada não existe ou não pertence à sua empresa"));
    }

    private Usuario usuarioDaMesmaEmpresa(Usuario usuarioRecebido, Integer empresaId) {
        if (usuarioRecebido == null || usuarioRecebido.getId() == null) {
            throw new IllegalArgumentException("O usuário é obrigatório");
        }
        return usuarioRepository.findByIdAndEmpresaId(usuarioRecebido.getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário informado não existe ou não pertence à sua empresa"));
    }
}
