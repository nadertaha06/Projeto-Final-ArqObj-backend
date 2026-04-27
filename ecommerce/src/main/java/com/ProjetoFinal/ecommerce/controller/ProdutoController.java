package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.produto.Produto;
import com.ProjetoFinal.ecommerce.model.produto.Categoria;
import com.ProjetoFinal.ecommerce.model.produto.Estoque;
import com.ProjetoFinal.ecommerce.model.usuario.Vendedor;
import com.ProjetoFinal.ecommerce.service.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody ProdutoRequest request) {
        Produto criado = produtoService.criar(request.toModel());
        return ResponseEntity.created(URI.create("/api/produtos/" + criado.getId())).body(ProdutoResponse.from(criado));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodos(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(produtoService.buscarPorNome(nome).stream().map(ProdutoResponse::from).toList());
        }
        return ResponseEntity.ok(produtoService.listarTodos().stream().map(ProdutoResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ProdutoResponse.from(produtoService.buscarPorId(id)));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<ProdutoResponse>> buscarPorVendedor(@PathVariable Long vendedorId) {
        return ResponseEntity.ok(produtoService.buscarPorVendedor(vendedorId).stream().map(ProdutoResponse::from).toList());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponse>> buscarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(categoriaId).stream().map(ProdutoResponse::from).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(ProdutoResponse.from(produtoService.atualizar(id, request.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<ProdutoResponse> atualizarStatusAtivo(@PathVariable Long id, @RequestParam Boolean ativo) {
        return ResponseEntity.ok(ProdutoResponse.from(produtoService.atualizarStatusAtivo(id, ativo)));
    }

    private record ProdutoRequest(
            String nome,
            String descricao,
            BigDecimal preco,
            String imagemUrl,
            IdRef categoria,
            IdRef vendedor
    ) {
        private Produto toModel() {
            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setImagemUrl(imagemUrl);
            if (categoria != null && categoria.id != null) {
                Categoria c = new Categoria();
                c.setId(categoria.id);
                produto.setCategoria(c);
            }
            if (vendedor != null && vendedor.id != null) {
                Vendedor v = new Vendedor();
                v.setId(vendedor.id);
                produto.setVendedor(v);
            }
            return produto;
        }
    }

    private record IdRef(Long id) {}

    private record ProdutoResponse(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            String imagemUrl,
            Boolean ativo,
            CategoriaResponse categoria,
            EstoqueResponse estoque,
            VendedorResponse vendedor
    ) {
        private static ProdutoResponse from(Produto produto) {
            return new ProdutoResponse(
                    produto.getId(),
                    produto.getNome(),
                    produto.getDescricao(),
                    produto.getPreco(),
                    produto.getImagemUrl(),
                    produto.getAtivo(),
                    CategoriaResponse.from(produto.getCategoria()),
                    EstoqueResponse.from(produto.getEstoque()),
                    VendedorResponse.from(produto.getVendedor())
            );
        }
    }

    private record CategoriaResponse(Long id, String nome) {
        private static CategoriaResponse from(Categoria categoria) {
            if (categoria == null) return null;
            return new CategoriaResponse(categoria.getId(), categoria.getNome());
        }
    }

    private record EstoqueResponse(Long id, Integer quantidade, Integer quantidadeMinima) {
        private static EstoqueResponse from(Estoque estoque) {
            if (estoque == null) return null;
            return new EstoqueResponse(estoque.getId(), estoque.getQuantidade(), estoque.getQuantidadeMinima());
        }
    }

    private record VendedorResponse(Long id, String nome, String nomeLoja) {
        private static VendedorResponse from(Vendedor vendedor) {
            if (vendedor == null) return null;
            return new VendedorResponse(vendedor.getId(), vendedor.getNome(), vendedor.getNomeLoja());
        }
    }
}
