package com.easy.eats.cupom.model;

import java.time.LocalDateTime;

import com.easy.eats.empresa.model.model.Empresa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "TBCUPOM", uniqueConstraints = @UniqueConstraint(columnNames = { "empresa_id", "codigo" }))
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O código do cupom é obrigatório")
    private String codigo;

    // PERCENTUAL | VALOR_FIXO
    @NotBlank(message = "O tipo de desconto é obrigatório")
    private String tipoDesconto;

    @NotNull(message = "O valor do desconto é obrigatório")
    @Positive(message = "O valor do desconto deve ser maior que zero")
    private Double valorDesconto;

    private LocalDateTime dtValidadeInicio;
    private LocalDateTime dtValidadeFim;

    // Nulo = sem limite. Os dois juntos cobrem tanto "uso único" (1/1) quanto
    // "uso múltiplo" (limite alto ou nulo por cliente).
    private Integer limiteUsoTotal;
    private Integer limiteUsoPorCliente;

    private Double valorMinimoPedido;
    private Boolean flAtivo;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
