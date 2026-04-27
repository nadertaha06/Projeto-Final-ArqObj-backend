package com.ProjetoFinal.ecommerce.model.promocao;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "descontos_percentual")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescontoPercentual extends Desconto {

    @Column(nullable = false)
    private BigDecimal percentual;
}
