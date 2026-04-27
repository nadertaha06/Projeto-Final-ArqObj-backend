package com.ProjetoFinal.ecommerce.model.promocao;

import com.ProjetoFinal.ecommerce.model.produto.Produto;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cupom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private BigDecimal desconto;

    @Column(nullable = false)
    private LocalDate dataValidade;

    @Column(nullable = false)
    private Integer usoMaximo;

    @Column(nullable = false)
    private Integer usoAtual;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;
}
