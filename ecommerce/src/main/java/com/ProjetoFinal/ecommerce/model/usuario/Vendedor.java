package com.ProjetoFinal.ecommerce.model.usuario;

import com.ProjetoFinal.ecommerce.model.produto.Produto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vendedores")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendedor extends Usuario {

    @Column(nullable = false)
    private String nomeLoja;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("vendedor-produtos")
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Double avaliacao = 0.0;
}
