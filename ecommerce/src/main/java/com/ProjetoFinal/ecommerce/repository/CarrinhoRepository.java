package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.pedido.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByClienteId(Long clienteId);
}
