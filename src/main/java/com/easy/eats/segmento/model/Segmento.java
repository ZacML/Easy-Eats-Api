package com.easy.eats.segmento.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Categoria de negócio de uma empresa (ex.: Food Truck, Restaurante,
 * Delivery). Determina quais módulos do sistema ficam disponíveis para as
 * empresas vinculadas a ela. Cadastrada e mantida pelo superadmin.
 */
@Entity
@Table(name = "TBSEGMENTO")
@Getter
@Setter
@NoArgsConstructor
public class Segmento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome do segmento é obrigatório")
    private String nome;

    private String descricao;

    private Boolean flAtivo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "TBSEGMENTO_FUNCIONALIDADE", joinColumns = @JoinColumn(name = "segmento_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "funcionalidade")
    private Set<Funcionalidade> funcionalidades = new HashSet<>();
}
