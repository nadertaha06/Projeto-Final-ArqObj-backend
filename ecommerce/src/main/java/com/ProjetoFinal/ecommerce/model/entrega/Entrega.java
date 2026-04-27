package com.ProjetoFinal.ecommerce.model.entrega;

import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.usuario.Endereco;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference("pedido-entrega")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco enderecoDestino;

    private String codigoRastreio;

    private LocalDate previsaoEntrega;

    private LocalDateTime dataEntrega;

    @Column(nullable = false)
    private String status;
}
