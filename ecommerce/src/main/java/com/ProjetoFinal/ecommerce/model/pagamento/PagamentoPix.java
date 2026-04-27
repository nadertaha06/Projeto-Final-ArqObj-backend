package com.ProjetoFinal.ecommerce.model.pagamento;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pagamentos_pix")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoPix extends Pagamento {

    private String chavePix;

    private String txid;
}
