package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByVendedorId(Long vendedorId);
    @Query("select p from Produto p where p.categoria.id = :categoriaId and (p.ativo = true or p.ativo is null)")
    List<Produto> findPublicByCategoriaId(Long categoriaId);
    @Query("select p from Produto p where lower(p.nome) like lower(concat('%', :nome, '%')) and (p.ativo = true or p.ativo is null)")
    List<Produto> findPublicByNomeContainingIgnoreCase(String nome);
    @Query("select p from Produto p where p.ativo = true or p.ativo is null")
    List<Produto> findAllPublic();
}
