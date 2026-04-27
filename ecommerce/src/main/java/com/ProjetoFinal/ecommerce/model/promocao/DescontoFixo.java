package com.ProjetoFinal.ecommerce.model.promocao;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "descontos_fixo")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescontoFixo extends Desconto {

    @Column(nullable = false)
    private BigDecimal valorFixo;
}
