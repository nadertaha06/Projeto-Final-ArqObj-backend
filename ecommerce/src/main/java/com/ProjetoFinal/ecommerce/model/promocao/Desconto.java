package com.ProjetoFinal.ecommerce.model.promocao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "descontos")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Desconto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;
}
