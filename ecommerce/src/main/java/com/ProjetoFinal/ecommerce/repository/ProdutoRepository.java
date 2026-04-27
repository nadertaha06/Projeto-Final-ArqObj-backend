package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByVendedorId(Long vendedorId);
    List<Produto> findByCategoriaId(Long categoriaId);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
