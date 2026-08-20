package com.easy.eats.venda.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioFaturamento {

    private Double totalFaturado;
    private Double ticketMedio;
    private Integer quantidadeVendas;

}
