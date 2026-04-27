package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.produto.Produto;
import com.ProjetoFinal.ecommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto criar(Produto produto) {
        if (produto.getAtivo() == null) {
            produto.setAtivo(true);
        }
        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAllPublic();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + id));
    }

    public List<Produto> buscarPorVendedor(Long vendedorId) {
        return produtoRepository.findByVendedorId(vendedorId);
    }

    public List<Produto> buscarPorCategoria(Long categoriaId) {
        return produtoRepository.findPublicByCategoriaId(categoriaId);
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoRepository.findPublicByNomeContainingIgnoreCase(nome);
    }

    public Produto atualizar(Long id, Produto dadosAtualizados) {
        Produto produto = buscarPorId(id);
        produto.setNome(dadosAtualizados.getNome());
        produto.setDescricao(dadosAtualizados.getDescricao());
        produto.setPreco(dadosAtualizados.getPreco());
        produto.setImagemUrl(dadosAtualizados.getImagemUrl());
        produto.setCategoria(dadosAtualizados.getCategoria());
        return produtoRepository.save(produto);
    }

    public Produto atualizarStatusAtivo(Long id, Boolean ativo) {
        Produto produto = buscarPorId(id);
        produto.setAtivo(ativo);
        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        atualizarStatusAtivo(id, false);
    }
}
