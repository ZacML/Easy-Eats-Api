package com.easy.eats.cardapio.model;

import java.time.LocalDateTime;

import com.easy.eats.empresa.model.model.Empresa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TBCARDAPIO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome do cardápio é obrigatório")
    private String nome;

    private String descricao;
    private Boolean flAtivo;
    private LocalDateTime dtAlteracao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

}