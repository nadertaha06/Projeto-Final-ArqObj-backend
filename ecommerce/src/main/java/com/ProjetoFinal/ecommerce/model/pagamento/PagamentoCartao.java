package com.ProjetoFinal.ecommerce.model.pagamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagamentos_cartao")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoCartao extends Pagamento {

    @Column(length = 4)
    private String ultimosQuatroDigitos;

    private String nomeTitular;

    private Integer parcelas;
}
