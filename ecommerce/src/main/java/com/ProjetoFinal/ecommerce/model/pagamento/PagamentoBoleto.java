package com.ProjetoFinal.ecommerce.model.pagamento;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos_boleto")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoBoleto extends Pagamento {

    private String codigoBarras;

    private LocalDate dataVencimento;
}
