package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.produto.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProdutoId(Long produtoId);
}
