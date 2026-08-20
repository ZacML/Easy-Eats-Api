package com.easy.eats.caixa.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easy.eats.caixa.model.Caixa;
import com.easy.eats.caixa.repository.CaixaRepository;
import com.easy.eats.empresa.repository.EmpresaRepository;
import com.easy.eats.movimentacaoFinanceira.model.MovimentacaoFinanceira;
import com.easy.eats.movimentacaoFinanceira.repository.MovimentacaoFinanceiraRepository;
import com.easy.eats.pagamento.repository.PagamentoRepository;
import com.easy.eats.security.SecurityUtils;
import com.easy.eats.usuario.repository.UsuarioRepository;

@Service
public class CaixaService {

    @Autowired
    CaixaRepository repository;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Autowired
    MovimentacaoFinanceiraRepository movimentacaoRepository;

    /** Caixa aberto da empresa, ou null se nenhum estiver aberto agora. */
    public Caixa status() {
        return repository.findByEmpresaIdAndStatus(SecurityUtils.getEmpresaId(), "ABERTO").orElse(null);
    }

    /** Sessões já fechadas cujo fechamento caiu dentro de [inicio, fim], mais recentes primeiro. */
    public java.util.List<Caixa> historico(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findAllByEmpresaIdAndStatusAndDtFechamentoBetweenOrderByDtFechamentoDesc(
                SecurityUtils.getEmpresaId(), "FECHADO", inicio, fim);
    }

    public Caixa abrir(Double valorInicial, String observacoes) {
        Integer empresaId = SecurityUtils.getEmpresaId();

        if (repository.findByEmpresaIdAndStatus(empresaId, "ABERTO").isPresent()) {
            throw new IllegalArgumentException("Já existe um caixa aberto para esta empresa");
        }
        if (valorInicial == null || valorInicial < 0) {
            throw new IllegalArgumentException("Informe o valor inicial do caixa");
        }

        Caixa caixa = new Caixa();
        caixa.setStatus("ABERTO");
        caixa.setValorInicial(valorInicial);
        caixa.setObservacoesAbertura(observacoes);
        caixa.setDtAbertura(LocalDateTime.now());
        caixa.setUsuarioAbertura(
                usuarioRepository.getReferenceById(SecurityUtils.getUsuarioAutenticado().getUsuarioId()));
        caixa.setEmpresa(empresaRepository.getReferenceById(empresaId));

        return repository.save(caixa);
    }

    public Caixa fechar(Integer id, Double valorApuradoInformado, String observacoes) {
        Caixa caixa = buscarPorId(id);
        if (!"ABERTO".equalsIgnoreCase(caixa.getStatus())) {
            throw new IllegalArgumentException("Este caixa já está fechado");
        }

        double totalPagamentos = pagamentoRepository.findAllByCaixaId(id).stream()
                .mapToDouble(p -> p.getValor() != null ? p.getValor() : 0.0)
                .sum();

        double totalSuprimento = 0.0;
        double totalSangria = 0.0;
        for (MovimentacaoFinanceira mov : movimentacaoRepository.findAllByCaixaId(id)) {
            double valor = mov.getValor() != null ? mov.getValor() : 0.0;
            if ("SUPRIMENTO".equalsIgnoreCase(mov.getTipo())) {
                totalSuprimento += valor;
            } else if ("SANGRIA".equalsIgnoreCase(mov.getTipo())) {
                totalSangria += valor;
            }
        }

        double valorApuradoSistema = caixa.getValorInicial() + totalPagamentos + totalSuprimento - totalSangria;

        caixa.setValorApuradoSistema(valorApuradoSistema);
        caixa.setValorApuradoInformado(valorApuradoInformado);
        caixa.setDiferenca(valorApuradoInformado != null ? valorApuradoInformado - valorApuradoSistema : null);
        caixa.setObservacoesFechamento(observacoes);
        caixa.setStatus("FECHADO");
        caixa.setDtFechamento(LocalDateTime.now());
        caixa.setUsuarioFechamento(
                usuarioRepository.getReferenceById(SecurityUtils.getUsuarioAutenticado().getUsuarioId()));

        return repository.save(caixa);
    }

    public MovimentacaoFinanceira registrarMovimentacao(Integer caixaId, String tipo, Double valor,
            String descricao) {
        Caixa caixa = buscarPorId(caixaId);
        if (!"ABERTO".equalsIgnoreCase(caixa.getStatus())) {
            throw new IllegalArgumentException("O caixa não está aberto");
        }
        if (tipo == null || (!"SANGRIA".equalsIgnoreCase(tipo) && !"SUPRIMENTO".equalsIgnoreCase(tipo))) {
            throw new IllegalArgumentException("Tipo de movimentação inválido, use SANGRIA ou SUPRIMENTO");
        }
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Informe um valor maior que zero");
        }

        MovimentacaoFinanceira movimentacao = new MovimentacaoFinanceira();
        movimentacao.setTipo(tipo.toUpperCase());
        movimentacao.setCategoria("CAIXA");
        movimentacao.setValor(valor);
        movimentacao.setDescricao(descricao);
        movimentacao.setCaixa(caixa);
        movimentacao.setEmpresa(caixa.getEmpresa());

        return movimentacaoRepository.save(movimentacao);
    }

    private Caixa buscarPorId(Integer id) {
        Caixa caixa = SecurityUtils.isSuperadmin()
                ? repository.findById(id).orElse(null)
                : repository.findByIdAndEmpresaId(id, SecurityUtils.getEmpresaId()).orElse(null);

        if (caixa == null) {
            throw new RuntimeException("Caixa não encontrado");
        }
        return caixa;
    }
}
