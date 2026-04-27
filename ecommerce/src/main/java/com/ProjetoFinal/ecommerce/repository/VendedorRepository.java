package com.ProjetoFinal.ecommerce.repository;

import com.ProjetoFinal.ecommerce.model.usuario.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    Optional<Vendedor> findByEmail(String email);
    Optional<Vendedor> findByNomeLoja(String nomeLoja);
}
