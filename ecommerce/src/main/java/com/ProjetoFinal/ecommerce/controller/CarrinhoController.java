package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.pedido.Carrinho;
import com.ProjetoFinal.ecommerce.model.pedido.ItemCarrinho;
import com.ProjetoFinal.ecommerce.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<CarrinhoResponse> buscarCarrinho(@PathVariable Long clienteId) {
        Carrinho carrinho = carrinhoService.buscarOuCriarCarrinho(clienteId);
        return ResponseEntity.ok(CarrinhoResponse.from(carrinho));
    }

    @PostMapping("/{clienteId}/itens")
    public ResponseEntity<CarrinhoResponse> adicionarItem(
            @PathVariable Long clienteId,
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {
        Carrinho carrinho = carrinhoService.adicionarItem(clienteId, produtoId, quantidade);
        return ResponseEntity.ok(CarrinhoResponse.from(carrinho));
    }

    @DeleteMapping("/{clienteId}/itens/{produtoId}")
    public ResponseEntity<CarrinhoResponse> removerItem(
            @PathVariable Long clienteId,
            @PathVariable Long produtoId) {
        Carrinho carrinho = carrinhoService.removerItem(clienteId, produtoId);
        return ResponseEntity.ok(CarrinhoResponse.from(carrinho));
    }

    @DeleteMapping("/{clienteId}/limpar")
    public ResponseEntity<CarrinhoResponse> limpar(@PathVariable Long clienteId) {
        Carrinho carrinho = carrinhoService.limpar(clienteId);
        return ResponseEntity.ok(CarrinhoResponse.from(carrinho));
    }

    private record CarrinhoResponse(Long id, List<ItemCarrinhoResponse> itens) {
        private static CarrinhoResponse from(Carrinho carrinho) {
            List<ItemCarrinhoResponse> itens = carrinho.getItens()
                    .stream()
                    .map(ItemCarrinhoResponse::from)
                    .toList();
            return new CarrinhoResponse(carrinho.getId(), itens);
        }
    }

    private record ItemCarrinhoResponse(
            Long id,
            ProdutoCarrinhoResponse produto,
            Integer quantidade
    ) {
        private static ItemCarrinhoResponse from(ItemCarrinho item) {
            return new ItemCarrinhoResponse(
                    item.getId(),
                    ProdutoCarrinhoResponse.from(item),
                    item.getQuantidade()
            );
        }
    }

    private record ProdutoCarrinhoResponse(
            Long id,
            String nome,
            BigDecimal preco,
            String imagemUrl
    ) {
        private static ProdutoCarrinhoResponse from(ItemCarrinho item) {
            var produto = item.getProduto();
            return new ProdutoCarrinhoResponse(
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getImagemUrl()
            );
        }
    }
}
