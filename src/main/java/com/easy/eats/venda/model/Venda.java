package com.easy.eats.venda.model;

import java.time.LocalDateTime;
import java.util.List;

import com.easy.eats.cliente.model.Cliente;
import com.easy.eats.comanda.model.Comanda;
import com.easy.eats.cupom.model.Cupom;
import com.easy.eats.empresa.model.model.Empresa;
import com.easy.eats.itemVenda.model.ItemVenda;
import com.easy.eats.mesa.model.Mesa;
import com.easy.eats.pagamento.model.Pagamento;
import com.easy.eats.usuario.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBVENDA")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O status da venda é obrigatório")
    private String status;

    @NotBlank(message = "O tipo da venda é obrigatório")
    private String tipo;

    private String origem;

    // Recalculado automaticamente pelo VendaService sempre que um item é
    // criado/alterado/removido: soma de ItemVenda.valor_total menos desconto.
    private Double valor_total;

    // Soma de todo desconto aplicado (cupom + cashback resgatado).
    private Double desconto;

    // Antes eram String (dt_criacao/dt_alteracao) e nunca eram preenchidos —
    // nenhuma consulta por período era possível. Ver histórico do módulo
    // Financeiro para o levantamento original desse problema.
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAlteracao;

    @PrePersist
    private void aoCriar() {
        LocalDateTime agora = LocalDateTime.now();
        if (dataCriacao == null) {
            dataCriacao = agora;
        }
        dataAlteracao = agora;
    }

    @PreUpdate
    private void aoAtualizar() {
        dataAlteracao = LocalDateTime.now();
    }

    /**
     * Cliente cadastrado vinculado ao pedido (opcional — pedidos de
     * balcão/mesa sem cadastro continuam identificados só por
     * {@link #nomeCliente}). Necessário para aplicar limite de cupom por
     * cliente e para acúmulo/resgate de cashback.
     */
    @JsonIgnoreProperties({ "enderecos" })
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /** Cupom aplicado a esta venda, se houver. */
    @ManyToOne
    @JoinColumn(name = "cupom_id")
    private Cupom cupom;

    // Evita creditar cashback mais de uma vez quando a venda recebe mais de
    // um pagamento (ex.: pagamento dividido).
    private Boolean cashbackAcumulado;

    @JsonIgnoreProperties("venda")
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens;

    @JsonIgnoreProperties("venda")
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<Pagamento> pagamentos;

    /**
     * Opcional: pedido de mesa (atendimento no salão). Pedidos de balcão/retirada
     * não têm mesa — nesse caso o cliente é identificado por {@link #nomeCliente}.
     * Pelo menos um dos dois deve estar preenchido (validado no service).
     */
    @JsonIgnoreProperties("vendas")
    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    /**
     * Nome do cliente para pedidos sem mesa (balcão/retirada/delivery).
     */
    private String nomeCliente;

    /**
     * Opcional: comanda à qual esta rodada de pedido pertence (módulo de
     * Comandas). Pedidos de balcão/retirada continuam sem comanda.
     */
    @JsonIgnoreProperties({ "vendas", "mesa" })
    @ManyToOne
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;

    @NotNull(message = "O usuário é obrigatório")
    @ManyToOne
    @JoinColumn(name = "Usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

}
