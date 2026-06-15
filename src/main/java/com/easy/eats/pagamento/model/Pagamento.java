package com.easy.eats.pagamento.model;

import com.easy.eats.venda.model.Venda;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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
    private String metodo;
    private Double valor;
    private String status;
    private String dt_pagamento;
    private String dt_alteracao;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;
}
