package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.produto.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
    List<Avaliacao> findByProdutoId(Long produtoId);
    List<Avaliacao> findByClienteId(Long clienteId);
    boolean existsByClienteIdAndProdutoId(Long clienteId, Long produtoId);

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.produto.id = :produtoId")
    Double calcularMediaNotasPorProduto(Long produtoId);
}
