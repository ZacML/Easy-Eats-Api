package com.easy.eats.venda.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TBVENDA")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String status;
    private String tipo;
    private String origem;
    private Double valor_total;
    private Double desconto;
    private String dt_fechamento;
    private String dt_criacao;
    private String dt_alteracao;

    public Venda(Integer id, String status, String tipo, String origem, Double valor_total, Double desconto,
            String dt_fechamento, String dt_criacao, String dt_alteracao) {
        this.id = id;
        this.status = status;
        this.tipo = tipo;
        this.origem = origem;
        this.valor_total = valor_total;
        this.desconto = desconto;
        this.dt_fechamento = dt_fechamento;
        this.dt_criacao = dt_criacao;
        this.dt_alteracao = dt_alteracao;
    }

}
