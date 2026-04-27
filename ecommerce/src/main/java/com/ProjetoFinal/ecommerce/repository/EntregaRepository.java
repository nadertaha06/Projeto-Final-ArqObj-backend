package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.entrega.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    Optional<Entrega> findByPedidoId(Long pedidoId);
    Optional<Entrega> findByCodigoRastreio(String codigoRastreio);
}
