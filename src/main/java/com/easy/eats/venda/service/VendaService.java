package com.easy.eats.venda.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.cashback.model.CashbackConfig;
import com.easy.eats.cashback.service.CashbackConfigService;
import com.easy.eats.cliente.model.Cliente;
import com.easy.eats.cliente.repository.ClienteRepository;
import com.easy.eats.comanda.model.Comanda;
import com.easy.eats.comanda.repository.ComandaRepository;
import com.easy.eats.cupom.model.Cupom;
import com.easy.eats.cupom.model.CupomUso;
import com.easy.eats.cupom.repository.CupomRepository;
import com.easy.eats.cupom.repository.CupomUsoRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.itemVenda.model.ItemVenda;
import com.easy.eats.itemVenda.repository.ItemVendaRepository;
import com.easy.eats.itemVenda.service.ItemVendaService;
import com.easy.eats.mesa.model.Mesa;
import com.easy.eats.mesa.repository.MesaRepository;
import com.easy.eats.security.SecurityUtils;
import com.easy.eats.usuario.model.Usuario;
import com.easy.eats.usuario.repository.UsuarioRepository;
import com.easy.eats.venda.model.RelatorioFaturamento;
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
    ItemVendaRepository itemVendaRepository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    MesaRepository mesaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ComandaRepository comandaRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    CupomRepository cupomRepository;

    @Autowired
    CupomUsoRepository cupomUsoRepository;

    @Autowired
    CashbackConfigService cashbackConfigService;

    public Venda criar(Venda venda) {
        Integer empresaId = SecurityUtils.getEmpresaId();

        boolean temMesa = venda.getMesa() != null && venda.getMesa().getId() != null;
        boolean temNomeCliente = venda.getNomeCliente() != null && !venda.getNomeCliente().isBlank();
        boolean temCliente = venda.getCliente() != null && venda.getCliente().getId() != null;

        if (!temMesa && !temNomeCliente && !temCliente) {
            throw new IllegalArgumentException("Informe a mesa, o cliente cadastrado ou o nome do cliente");
        }

        venda.setId(null);
        venda.setEmpresa(empresaRepository.getReferenceById(empresaId));
        venda.setMesa(temMesa ? mesaDaMesmaEmpresa(venda.getMesa(), empresaId) : null);
        venda.setUsuario(usuarioDaMesmaEmpresa(venda.getUsuario(), empresaId));
        venda.setComanda(comandaDaMesmaEmpresa(venda.getComanda(), empresaId));
        venda.setCliente(temCliente ? clienteDaMesmaEmpresa(venda.getCliente(), empresaId) : null);
        venda.setCupom(null);
        venda.setValor_total(0.0);
        venda.setDesconto(0.0);

        return repository.save(venda);
    }

    /**
     * Recalcula {@code valor_total} somando os itens atuais da venda e
     * descontando {@code desconto} (cupom + cashback resgatado). Chamado pelo
     * {@link ItemVendaService} sempre que um item é criado, alterado ou
     * removido — antes disso o campo nunca era preenchido de verdade.
     */
    public Venda recalcularTotal(Integer vendaId) {
        Venda venda = repository.findById(vendaId)
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        double totalItens = somaItens(vendaId);

        double desconto = venda.getDesconto() != null ? venda.getDesconto() : 0.0;
        venda.setValor_total(Math.max(0.0, totalItens - desconto));

        return repository.save(venda);
    }

    /**
     * Valida e aplica um cupom à venda: vigência, ativo, valor mínimo do
     * pedido (contra a soma atual dos itens) e limites de uso (total e por
     * cliente, via {@link CupomUso} — antes disso nada contava o uso de um
     * cupom em lugar nenhum do sistema). Registra o uso e recalcula o total.
     */
    public Venda aplicarCupom(Integer vendaId, String codigo) {
        Venda venda = repository.findByIdAndEmpresaId(vendaId, SecurityUtils.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        if (venda.getCupom() != null) {
            throw new IllegalArgumentException("Esta venda já tem um cupom aplicado");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("Informe o código do cupom");
        }

        Cupom cupom = cupomRepository.findByCodigoIgnoreCaseAndEmpresaId(codigo.trim(), venda.getEmpresa().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado"));

        if (cupom.getFlAtivo() == null || !cupom.getFlAtivo()) {
            throw new IllegalArgumentException("Este cupom não está ativo");
        }

        LocalDateTime agora = LocalDateTime.now();
        if (cupom.getDtValidadeInicio() != null && agora.isBefore(cupom.getDtValidadeInicio())) {
            throw new IllegalArgumentException("Este cupom ainda não é válido");
        }
        if (cupom.getDtValidadeFim() != null && agora.isAfter(cupom.getDtValidadeFim())) {
            throw new IllegalArgumentException("Este cupom está expirado");
        }

        double totalItens = somaItens(vendaId);

        if (cupom.getValorMinimoPedido() != null && totalItens < cupom.getValorMinimoPedido()) {
            throw new IllegalArgumentException(
                    "O pedido precisa ter no mínimo " + cupom.getValorMinimoPedido() + " para usar este cupom");
        }

        if (cupom.getLimiteUsoTotal() != null
                && cupomUsoRepository.countByCupomId(cupom.getId()) >= cupom.getLimiteUsoTotal()) {
            throw new IllegalArgumentException("Este cupom atingiu o limite de usos");
        }

        Integer clienteId = venda.getCliente() != null ? venda.getCliente().getId() : null;
        if (clienteId != null && cupom.getLimiteUsoPorCliente() != null
                && cupomUsoRepository.countByCupomIdAndClienteId(cupom.getId(), clienteId) >= cupom
                        .getLimiteUsoPorCliente()) {
            throw new IllegalArgumentException("Você já atingiu o limite de usos deste cupom");
        }

        double desconto = "PERCENTUAL".equalsIgnoreCase(cupom.getTipoDesconto())
                ? totalItens * (cupom.getValorDesconto() / 100.0)
                : Math.min(cupom.getValorDesconto(), totalItens);

        venda.setCupom(cupom);
        venda.setDesconto((venda.getDesconto() != null ? venda.getDesconto() : 0.0) + desconto);
        venda.setValor_total(Math.max(0.0, totalItens - venda.getDesconto()));
        Venda salva = repository.save(venda);

        CupomUso uso = new CupomUso();
        uso.setCupom(cupom);
        uso.setVenda(salva);
        uso.setCliente(venda.getCliente());
        cupomUsoRepository.save(uso);

        return salva;
    }

    /**
     * Resgata parte do saldo de cashback do cliente da venda, abatendo do
     * total. O cashback é acumulado automaticamente pelo
     * {@code PagamentoService} quando a venda é paga — aqui só o resgate
     * (uso do saldo já acumulado antes) é tratado.
     */
    public Venda resgatarCashback(Integer vendaId, Double valor) {
        Venda venda = repository.findByIdAndEmpresaId(vendaId, SecurityUtils.getEmpresaId())
                .orElseThrow(() -> new IllegalArgumentException("Venda não encontrada"));

        if (venda.getCliente() == null) {
            throw new IllegalArgumentException("Esta venda não está vinculada a um cliente cadastrado");
        }
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Informe um valor maior que zero");
        }

        Cliente cliente = venda.getCliente();
        double saldoAtual = cliente.getSaldoCashback() != null ? cliente.getSaldoCashback() : 0.0;
        if (valor > saldoAtual) {
            throw new IllegalArgumentException("Saldo de cashback insuficiente");
        }

        double totalItens = somaItens(vendaId);

        double descontoAtual = venda.getDesconto() != null ? venda.getDesconto() : 0.0;
        double valorResgatavel = Math.min(valor, Math.max(0.0, totalItens - descontoAtual));

        cliente.setSaldoCashback(saldoAtual - valorResgatavel);
        clienteRepository.save(cliente);

        venda.setDesconto(descontoAtual + valorResgatavel);
        venda.setValor_total(Math.max(0.0, totalItens - venda.getDesconto()));

        return repository.save(venda);
    }

    /**
     * Credita cashback ao cliente da venda de acordo com a
     * {@link CashbackConfig} da empresa — chamado pelo {@code PagamentoService}
     * quando um pagamento é registrado. Idempotente por venda (ver
     * {@link Venda#getCashbackAcumulado()}) para não creditar duas vezes em
     * pagamento dividido.
     */
    public void creditarCashbackSePreciso(Venda venda) {
        if (venda == null || venda.getCliente() == null) {
            return;
        }
        if (Boolean.TRUE.equals(venda.getCashbackAcumulado())) {
            return;
        }

        CashbackConfig config = cashbackConfigService.buscarOuCriar();
        if (config.getFlAtivo() == null || !config.getFlAtivo()) {
            return;
        }

        double valorVenda = venda.getValor_total() != null ? venda.getValor_total() : 0.0;
        double minimo = config.getValorMinimoParaAcumular() != null ? config.getValorMinimoParaAcumular() : 0.0;
        if (valorVenda < minimo) {
            return;
        }

        double credito = valorVenda * (config.getPercentualAcumulo() / 100.0);
        if (credito <= 0) {
            return;
        }

        Cliente cliente = venda.getCliente();
        double saldoAtual = cliente.getSaldoCashback() != null ? cliente.getSaldoCashback() : 0.0;
        cliente.setSaldoCashback(saldoAtual + credito);
        clienteRepository.save(cliente);

        venda.setCashbackAcumulado(true);
        repository.save(venda);
    }

    /** Faturamento agregado da empresa no período [inicio, fim]. */
    public RelatorioFaturamento relatorioFaturamento(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = repository.findAllByEmpresaIdAndDataCriacaoBetween(
                SecurityUtils.getEmpresaId(), inicio, fim);

        double total = vendas.stream()
                .mapToDouble(v -> v.getValor_total() != null ? v.getValor_total() : 0.0)
                .sum();
        int quantidade = vendas.size();
        double ticketMedio = quantidade > 0 ? total / quantidade : 0.0;

        return new RelatorioFaturamento(total, ticketMedio, quantidade);
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

    private double somaItens(Integer vendaId) {
        return itemVendaRepository.findAllByVenda_Id(vendaId).stream()
                .mapToDouble(item -> item.getValor_total() != null ? item.getValor_total() : 0.0)
                .sum();
    }

    private Cliente clienteDaMesmaEmpresa(Cliente clienteRecebido, Integer empresaId) {
        if (clienteRecebido == null || clienteRecebido.getId() == null) {
            return null;
        }
        return clienteRepository.findByIdAndEmpresaId(clienteRecebido.getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente informado não existe ou não pertence à sua empresa"));
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

    /**
     * Comanda é opcional (só pedidos de mesa vinculados ao módulo de Comandas a
     * usam). Validada aqui, e não só no ComandaService, para que POST /venda
     * também rejeite uma comanda de outra empresa ou já fechada informada
     * diretamente por quem chamar a API sem passar pelo fluxo de comandas.
     */
    private Comanda comandaDaMesmaEmpresa(Comanda comandaRecebida, Integer empresaId) {
        if (comandaRecebida == null || comandaRecebida.getId() == null) {
            return null;
        }
        Comanda comanda = comandaRepository.findByIdAndEmpresaId(comandaRecebida.getId(), empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Comanda informada não existe ou não pertence à sua empresa"));
        if (!"ABERTA".equalsIgnoreCase(comanda.getStatus())) {
            throw new IllegalArgumentException("A comanda informada não está aberta");
        }
        return comanda;
    }
}
