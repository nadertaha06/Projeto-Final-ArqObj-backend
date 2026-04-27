package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.promocao.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CupomRepository extends JpaRepository<Cupom, Long> {
    Optional<Cupom> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Cupom> findByProdutoVendedorId(Long vendedorId);
}
