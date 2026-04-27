package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.produto.Estoque;
import com.ProjetoFinal.ecommerce.model.produto.Produto;
import com.ProjetoFinal.ecommerce.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping
    public ResponseEntity<EstoqueResponse> criar(@RequestBody EstoqueRequest request) {
        Estoque criado = estoqueService.criar(request.toModel());
        return ResponseEntity.created(URI.create("/api/estoques/" + criado.getId())).body(EstoqueResponse.from(criado));
    }

    @GetMapping
    public ResponseEntity<List<EstoqueResponse>> listarTodos() {
        return ResponseEntity.ok(estoqueService.listarTodos().stream().map(EstoqueResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EstoqueResponse.from(estoqueService.buscarPorId(id)));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<EstoqueResponse> buscarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(EstoqueResponse.from(estoqueService.buscarPorProduto(produtoId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponse> atualizar(@PathVariable Long id, @RequestBody EstoqueRequest request) {
        return ResponseEntity.ok(EstoqueResponse.from(estoqueService.atualizar(id, request.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record EstoqueRequest(IdRef produto, Integer quantidade, Integer quantidadeMinima) {
        private Estoque toModel() {
            Estoque estoque = new Estoque();
            if (produto != null && produto.id != null) {
                Produto p = new Produto();
                p.setId(produto.id);
                estoque.setProduto(p);
            }
            estoque.setQuantidade(quantidade);
            estoque.setQuantidadeMinima(quantidadeMinima);
            return estoque;
        }
    }

    private record IdRef(Long id) {}

    private record EstoqueResponse(Long id, Integer quantidade, Integer quantidadeMinima, ProdutoResumo produto) {
        private static EstoqueResponse from(Estoque estoque) {
            return new EstoqueResponse(
                    estoque.getId(),
                    estoque.getQuantidade(),
                    estoque.getQuantidadeMinima(),
                    ProdutoResumo.from(estoque.getProduto())
            );
        }
    }

    private record ProdutoResumo(Long id, String nome) {
        private static ProdutoResumo from(Produto produto) {
            if (produto == null) return null;
            return new ProdutoResumo(produto.getId(), produto.getNome());
        }
    }
}
