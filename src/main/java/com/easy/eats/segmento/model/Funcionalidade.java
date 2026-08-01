package com.easy.eats.segmento.model;

/**
 * Catálogo fixo de módulos do sistema que podem ser habilitados por
 * segmento de negócio (ex.: Food Truck, Restaurante). A lista é definida
 * pela plataforma (superadmin), não pelo cliente.
 */
public enum Funcionalidade {
    OPERACAO,
    PEDIDO,
    COZINHA,
    DELIVERY,
    ESTOQUE,
    COMPRAS,
    FINANCEIRO,
    PRODUTOS,
    CLIENTES,
    USUARIOS,
    CONFIGURACOES
}
