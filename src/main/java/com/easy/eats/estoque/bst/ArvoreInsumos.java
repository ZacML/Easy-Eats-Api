package com.easy.eats.estoque.bst;

import com.easy.eats.estoque.model.Insumo;

public class ArvoreInsumos {

    private NoInsumo raiz;

    public void inserir(Insumo insumo) {
        raiz = inserirRecursivo(raiz, insumo);
    }

    private NoInsumo inserirRecursivo(NoInsumo atual, Insumo insumo) {

        if (atual == null) {
            return new NoInsumo(insumo);
        }

        if (insumo.getNome().compareToIgnoreCase(atual.insumo.getNome()) < 0) {
            atual.esquerda = inserirRecursivo(atual.esquerda, insumo);
        } else {
            atual.direita = inserirRecursivo(atual.direita, insumo);
        }

        return atual;
    }

    public Insumo buscar(String nome) {
        return buscarRecursivo(raiz, nome);
    }

    private Insumo buscarRecursivo(NoInsumo atual, String nome) {

        if (atual == null) {
            return null;
        }

        int comparacao =
                nome.compareToIgnoreCase(atual.insumo.getNome());

        if (comparacao == 0) {
            return atual.insumo;
        }

        if (comparacao < 0) {
            return buscarRecursivo(atual.esquerda, nome);
        }

        return buscarRecursivo(atual.direita, nome);
    }

    public boolean atualizar(String nome, Insumo novoInsumo) {

    Insumo encontrado = buscar(nome);

    if (encontrado == null) {
        return false;
    }

    encontrado.setNome(novoInsumo.getNome());
    encontrado.setQuantidade(novoInsumo.getQuantidade());
    encontrado.setUnidade(novoInsumo.getUnidade());

    return true;
}

public boolean remover(String nome) {

    if (buscar(nome) == null) {
        return false;
    }

    raiz = removerRecursivo(raiz, nome);
    return true;
}

private NoInsumo removerRecursivo(NoInsumo atual, String nome) {

    if (atual == null) {
        return null;
    }

    int comparacao =
            nome.compareToIgnoreCase(atual.insumo.getNome());

    if (comparacao < 0) {
        atual.esquerda = removerRecursivo(atual.esquerda, nome);
    } else if (comparacao > 0) {
        atual.direita = removerRecursivo(atual.direita, nome);
    } else {

        if (atual.esquerda == null) {
            return atual.direita;
        }

        if (atual.direita == null) {
            return atual.esquerda;
        }

        NoInsumo menor = encontrarMenor(atual.direita);

        atual.insumo = menor.insumo;

        atual.direita =
                removerRecursivo(atual.direita, menor.insumo.getNome());
    }

    return atual;
}

private NoInsumo encontrarMenor(NoInsumo no) {

    while (no.esquerda != null) {
        no = no.esquerda;
    }

    return no;
}

}