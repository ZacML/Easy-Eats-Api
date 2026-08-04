package com.easy.eats.mesa.model;

import java.util.List;

import com.easy.eats.empresa.model.model.Empresa;
import com.easy.eats.venda.model.Venda;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "TBMESA")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "O número da mesa é obrigatório")
    @Positive(message = "O número da mesa deve ser maior que zero")
    private Integer numero;

    @NotBlank(message = "O status da mesa é obrigatório")
    private String status;

    private String dt_alteracao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    private List<Venda> vendas;
}
