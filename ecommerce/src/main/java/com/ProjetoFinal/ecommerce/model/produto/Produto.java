package com.ProjetoFinal.ecommerce.model.produto;

import com.ProjetoFinal.ecommerce.model.usuario.Vendedor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 2000)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    private String imagemUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference("categoria-produtos")
    private Categoria categoria;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    @JsonManagedReference("produto-estoque")
    private Estoque estoque;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", nullable = false)
    @JsonBackReference("vendedor-produtos")
    private Vendedor vendedor;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    @JsonManagedReference("produto-avaliacoes")
    @Builder.Default
    private List<Avaliacao> avaliacoes = new ArrayList<>();
}
