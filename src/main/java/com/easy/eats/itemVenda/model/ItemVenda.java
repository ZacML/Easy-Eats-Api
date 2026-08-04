package com.easy.eats.itemVenda.model;

import com.easy.eats.produto.model.Produto;
import com.easy.eats.venda.model.Venda;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TBITEMVENDA")
public class ItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Double quantidade;

    @NotNull(message = "O preço unitário é obrigatório")
    @Positive(message = "O preço unitário deve ser maior que zero")
    private Double preco_unitario;

    private Double custo_unitario;
    private Double valor_total;
    private Double desconto;
    private String dt_alteracao;

    @NotNull(message = "A venda é obrigatória")
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @NotNull(message = "O produto é obrigatório")
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
}
