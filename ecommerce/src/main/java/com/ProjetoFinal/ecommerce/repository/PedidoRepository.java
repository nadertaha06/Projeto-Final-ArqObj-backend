package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(StatusPedido status);
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);

    @Query("""
            select distinct p
            from Pedido p
            left join fetch p.cliente cli
            left join fetch p.entrega ent
            left join fetch ent.enderecoDestino end
            left join fetch p.itens i
            left join fetch i.produto prod
            where p.id = :id
            """)
    Optional<Pedido> findDetailedById(Long id);

    @Query("""
            select distinct p
            from Pedido p
            left join fetch p.cliente cli
            left join fetch p.entrega ent
            left join fetch ent.enderecoDestino end
            left join fetch p.itens i
            left join fetch i.produto prod
            left join fetch prod.vendedor vend
            where p.cliente.id = :clienteId
            order by p.id desc
            """)
    List<Pedido> findDetailedByClienteId(Long clienteId);

    @Query("""
            select distinct p
            from Pedido p
            left join fetch p.cliente cli
            left join fetch p.entrega ent
            left join fetch ent.enderecoDestino end
            left join fetch p.itens i
            left join fetch i.produto prod
            left join fetch prod.vendedor vend
            where vend.id = :vendedorId
            order by p.id desc
            """)
    List<Pedido> findDetailedByVendedorId(Long vendedorId);

    @Query("""
            select distinct p
            from Pedido p
            left join fetch p.cliente cli
            left join fetch p.entrega ent
            left join fetch ent.enderecoDestino end
            left join fetch p.itens i
            left join fetch i.produto prod
            order by p.id desc
            """)
    List<Pedido> findAllDetailed();
}
