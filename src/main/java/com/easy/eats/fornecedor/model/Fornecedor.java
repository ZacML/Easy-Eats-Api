package com.easy.eats.fornecedor.model;

import java.time.LocalDateTime;

import com.easy.eats.empresa.model.model.Empresa;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TBFORNECEDOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome do fornecedor é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    private String cnpj;

    private String telefone;

    @Email(message = "E-mail inválido")
    private String email;

    private Boolean flAtivo;

    private LocalDateTime dtCriacao;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}