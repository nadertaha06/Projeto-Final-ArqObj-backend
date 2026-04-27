package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.pagamento.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByStatus(String status);
}
