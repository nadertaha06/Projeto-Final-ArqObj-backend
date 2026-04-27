package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.pedido.Carrinho;
import com.ProjetoFinal.ecommerce.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Carrinho> buscarCarrinho(@PathVariable Long clienteId) {
        return ResponseEntity.ok(carrinhoService.buscarOuCriarCarrinho(clienteId));
    }

    @PostMapping("/{clienteId}/itens")
    public ResponseEntity<Carrinho> adicionarItem(
            @PathVariable Long clienteId,
            @RequestParam Long produtoId,
            @RequestParam int quantidade) {
        return ResponseEntity.ok(carrinhoService.adicionarItem(clienteId, produtoId, quantidade));
    }

    @DeleteMapping("/{clienteId}/itens/{produtoId}")
    public ResponseEntity<Carrinho> removerItem(
            @PathVariable Long clienteId,
            @PathVariable Long produtoId) {
        return ResponseEntity.ok(carrinhoService.removerItem(clienteId, produtoId));
    }

    @DeleteMapping("/{clienteId}/limpar")
    public ResponseEntity<Carrinho> limpar(@PathVariable Long clienteId) {
        return ResponseEntity.ok(carrinhoService.limpar(clienteId));
    }
}
