package com.easy.eats.venda.model;

import java.util.List;

import com.easy.eats.itemVenda.model.ItemVenda;
import com.easy.eats.mesa.model.Mesa;
import com.easy.eats.pagamento.model.Pagamento;
import com.easy.eats.usuario.model.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "TBVENDA")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String status;
    private String tipo;
    private String origem;
    private Double valor_total;
    private Double desconto;
    private String dt_fechamento;
    private String dt_criacao;
    private String dt_alteracao;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<Pagamento> pagamentos;

    @ManyToOne 
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "Usuario_id", nullable = false)
    private Usuario usuario;

}
