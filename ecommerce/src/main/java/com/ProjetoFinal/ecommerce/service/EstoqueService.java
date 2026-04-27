package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.produto.Estoque;
import com.ProjetoFinal.ecommerce.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public Estoque criar(Estoque estoque) {
        return estoqueRepository.save(estoque);
    }

    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    public Estoque buscarPorId(Long id) {
        return estoqueRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Estoque não encontrado: " + id));
    }

    public Estoque buscarPorProduto(Long produtoId) {
        return estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Estoque não encontrado para o produto: " + produtoId));
    }

    public boolean verificarDisponibilidade(Long produtoId, int quantidadeSolicitada) {
        Estoque estoque = buscarPorProduto(produtoId);
        return estoque.getQuantidade() >= quantidadeSolicitada;
    }

    public void reservar(Long produtoId, int quantidade) {
        Estoque estoque = buscarPorProduto(produtoId);
        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + produtoId);
        }
        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        estoqueRepository.save(estoque);
    }

    public void devolver(Long produtoId, int quantidade) {
        Estoque estoque = buscarPorProduto(produtoId);
        estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        estoqueRepository.save(estoque);
    }

    public Estoque atualizar(Long id, Estoque dadosAtualizados) {
        Estoque estoque = buscarPorId(id);
        estoque.setQuantidade(dadosAtualizados.getQuantidade());
        estoque.setQuantidadeMinima(dadosAtualizados.getQuantidadeMinima());
        return estoqueRepository.save(estoque);
    }

    public void deletar(Long id) {
        if (!estoqueRepository.existsById(id)) {
            throw new NoSuchElementException("Estoque não encontrado: " + id);
        }
        estoqueRepository.deleteById(id);
    }
}
