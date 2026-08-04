package com.easy.eats.pagamento.model;

import com.easy.eats.venda.model.Venda;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
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
@Table(name = "TBPAGAMENTO")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O método de pagamento é obrigatório")
    private String metodo;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    private Double valor;

    @NotBlank(message = "O status é obrigatório")
    private String status;

    private String dt_pagamento;
    private String dt_alteracao;

    @NotNull(message = "A venda é obrigatória")
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
}
