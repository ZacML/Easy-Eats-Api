package com.easy.eats.itemVenda.model;

import com.easy.eats.composicaoItem.model.ComposicaoItem;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Registra que um item de composição do produto foi retirado deste item de
 * venda específico (toggle desligado no carrinho).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TBITEMVENDACOMPOSICAOREMOVIDA")
public class ItemVendaComposicaoRemovida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_venda_id", nullable = false)
    private ItemVenda itemVenda;

    @NotNull(message = "O item de composição é obrigatório")
    @ManyToOne
    @JoinColumn(name = "composicao_item_id", nullable = false)
    private ComposicaoItem composicaoItem;
}
