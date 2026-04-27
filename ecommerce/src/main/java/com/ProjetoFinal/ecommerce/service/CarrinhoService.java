package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.pedido.Carrinho;
import com.ProjetoFinal.ecommerce.model.pedido.ItemCarrinho;
import com.ProjetoFinal.ecommerce.model.produto.Produto;
import com.ProjetoFinal.ecommerce.model.usuario.Cliente;
import com.ProjetoFinal.ecommerce.repository.CarrinhoRepository;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final ClienteService clienteService;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ProdutoService produtoService,
                           EstoqueService estoqueService,
                           ClienteService clienteService) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
        this.clienteService = clienteService;
    }

    public Carrinho buscarOuCriarCarrinho(Long clienteId) {
        return carrinhoRepository.findDetailedByClienteId(clienteId).orElseGet(() -> {
            Cliente cliente = clienteService.buscarPorId(clienteId);
            Carrinho novo = Carrinho.builder().cliente(cliente).build();
            Carrinho salvo = carrinhoRepository.save(novo);
            return carrinhoRepository.findDetailedByClienteId(clienteId).orElse(salvo);
        });
    }

    public Carrinho buscarPorCliente(Long clienteId) {
        return carrinhoRepository.findDetailedByClienteId(clienteId)
                .orElseThrow(() -> new NoSuchElementException("Carrinho não encontrado para cliente: " + clienteId));
    }

    public Carrinho adicionarItem(Long clienteId, Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        Produto produto = produtoService.buscarPorId(produtoId);

        if (produto.getVendedor().getId().equals(clienteId)) {
            throw new IllegalStateException("Vendedor não pode comprar seus próprios produtos");
        }

        if (!estoqueService.verificarDisponibilidade(produtoId, quantidade)) {
            throw new IllegalStateException("Estoque insuficiente para o produto: " + produtoId);
        }

        Carrinho carrinho = buscarOuCriarCarrinho(clienteId);

        carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produtoId))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantidade(item.getQuantidade() + quantidade),
                        () -> {
                            ItemCarrinho novoItem = ItemCarrinho.builder()
                                    .carrinho(carrinho)
                                    .produto(produto)
                                    .quantidade(quantidade)
                                    .build();
                            carrinho.getItens().add(novoItem);
                        }
                );

        carrinhoRepository.save(carrinho);
        return buscarPorCliente(clienteId);
    }

    public Carrinho removerItem(Long clienteId, Long produtoId) {
        Carrinho carrinho = buscarPorCliente(clienteId);
        carrinho.getItens().removeIf(item -> item.getProduto().getId().equals(produtoId));
        carrinhoRepository.save(carrinho);
        return buscarPorCliente(clienteId);
    }

    public Carrinho limpar(Long clienteId) {
        Carrinho carrinho = buscarPorCliente(clienteId);
        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);
        return buscarPorCliente(clienteId);
    }
}
