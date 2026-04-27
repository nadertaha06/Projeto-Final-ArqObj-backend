package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.promocao.Cupom;
import com.ProjetoFinal.ecommerce.model.produto.Produto;
import com.ProjetoFinal.ecommerce.repository.CupomRepository;
import com.ProjetoFinal.ecommerce.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CupomService {

    private final CupomRepository cupomRepository;
    private final ProdutoRepository produtoRepository;

    public CupomService(CupomRepository cupomRepository, ProdutoRepository produtoRepository) {
        this.cupomRepository = cupomRepository;
        this.produtoRepository = produtoRepository;
    }

    public Cupom criar(Cupom cupom, Long produtoId, Long vendedorId) {
        if (cupomRepository.existsByCodigo(cupom.getCodigo())) {
            throw new IllegalArgumentException("Código de cupom já existe: " + cupom.getCodigo());
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + produtoId));
        if (!produto.getVendedor().getId().equals(vendedorId)) {
            throw new IllegalArgumentException("Você só pode criar cupom para os seus próprios produtos.");
        }

        cupom.setProduto(produto);
        cupom.setUsoAtual(0);
        cupom.setAtivo(true);
        return cupomRepository.save(cupom);
    }

    public List<Cupom> listarTodos() {
        return cupomRepository.findAll();
    }

    public Cupom buscarPorId(Long id) {
        return cupomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupom não encontrado: " + id));
    }

    public Cupom buscarPorCodigo(String codigo) {
        return cupomRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NoSuchElementException("Cupom não encontrado: " + codigo));
    }

    public List<Cupom> listarPorVendedor(Long vendedorId) {
        return cupomRepository.findByProdutoVendedorId(vendedorId);
    }

    public Cupom validar(String codigo, Long produtoId) {
        Cupom cupom = buscarPorCodigo(codigo);

        if (!cupom.getAtivo()) {
            throw new IllegalStateException("Cupom inativo: " + codigo);
        }
        if (LocalDate.now().isAfter(cupom.getDataValidade())) {
            throw new IllegalStateException("Cupom expirado: " + codigo);
        }
        if (cupom.getUsoAtual() >= cupom.getUsoMaximo()) {
            throw new IllegalStateException("Cupom atingiu o limite de usos: " + codigo);
        }
        if (!cupom.getProduto().getId().equals(produtoId)) {
            throw new IllegalStateException("Cupom não é válido para este produto.");
        }
        return cupom;
    }

    public Cupom consumir(String codigo, Long produtoId) {
        Cupom cupom = validar(codigo, produtoId);
        cupom.setUsoAtual(cupom.getUsoAtual() + 1);
        if (cupom.getUsoAtual() >= cupom.getUsoMaximo()) {
            cupom.setAtivo(false);
        }
        return cupomRepository.save(cupom);
    }

    public Cupom atualizar(Long id, Cupom dadosAtualizados) {
        Cupom cupom = buscarPorId(id);
        cupom.setDesconto(dadosAtualizados.getDesconto());
        cupom.setDataValidade(dadosAtualizados.getDataValidade());
        cupom.setUsoMaximo(dadosAtualizados.getUsoMaximo());
        cupom.setAtivo(dadosAtualizados.getAtivo());
        return cupomRepository.save(cupom);
    }

    public void deletar(Long id) {
        if (!cupomRepository.existsById(id)) {
            throw new NoSuchElementException("Cupom não encontrado: " + id);
        }
        cupomRepository.deleteById(id);
    }
}
