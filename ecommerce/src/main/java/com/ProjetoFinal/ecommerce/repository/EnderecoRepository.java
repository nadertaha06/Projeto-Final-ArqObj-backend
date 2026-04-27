package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.usuario.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findFirstByClienteIdOrderByIdAsc(Long clienteId);
}
