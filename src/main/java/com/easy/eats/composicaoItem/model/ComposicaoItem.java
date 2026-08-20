package com.easy.eats.composicaoItem.model;

import com.easy.eats.produto.model.Produto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Item de composição de um produto PREPARADO (ex.: "Queijo" no X-Burger),
 * exibido como toggle no carrinho para manter/retirar do pedido.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TBCOMPOSICAOITEM")
public class ComposicaoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome do item de composição é obrigatório")
    private String nome;

    private Boolean removivel;

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    @JsonIgnoreProperties({ "categoria", "empresa" })
    private Produto produto;
}
