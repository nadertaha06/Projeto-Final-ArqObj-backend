package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.usuario.Vendedor;
import com.ProjetoFinal.ecommerce.repository.VendedorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    public Vendedor criar(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    public List<Vendedor> listarTodos() {
        return vendedorRepository.findAll();
    }

    public Vendedor buscarPorId(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Vendedor não encontrado: " + id));
    }

    public Vendedor atualizar(Long id, Vendedor dadosAtualizados) {
        Vendedor vendedor = buscarPorId(id);
        vendedor.setNome(dadosAtualizados.getNome());
        vendedor.setNomeLoja(dadosAtualizados.getNomeLoja());
        vendedor.setTelefone(dadosAtualizados.getTelefone());
        return vendedorRepository.save(vendedor);
    }

    public void deletar(Long id) {
        if (!vendedorRepository.existsById(id)) {
            throw new NoSuchElementException("Vendedor não encontrado: " + id);
        }
        vendedorRepository.deleteById(id);
    }
}
