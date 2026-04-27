package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.pedido.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByClienteId(Long clienteId);

    @Query("""
            select distinct c
            from Carrinho c
            left join fetch c.itens i
            left join fetch i.produto p
            where c.cliente.id = :clienteId
            """)
    Optional<Carrinho> findDetailedByClienteId(Long clienteId);
}
