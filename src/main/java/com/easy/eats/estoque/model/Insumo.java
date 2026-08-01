package com.easy.eats.estoque.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Insumo {

    @NotBlank(message = "O nome do insumo é obrigatório")
    private String nome;

    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private int quantidade;

    @NotBlank(message = "A unidade é obrigatória")
    private String unidade;

    public Insumo() {
    }

    public Insumo(String nome, int quantidade, String unidade) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidade = unidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
}