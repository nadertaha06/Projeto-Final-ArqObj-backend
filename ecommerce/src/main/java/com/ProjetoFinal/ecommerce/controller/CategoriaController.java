package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.produto.Categoria;
import com.ProjetoFinal.ecommerce.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(@RequestBody CategoriaRequest request) {
        Categoria criada = categoriaService.criar(request.toModel());
        return ResponseEntity.created(URI.create("/api/categorias/" + criada.getId())).body(CategoriaResponse.from(criada));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas().stream().map(CategoriaResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id, @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.atualizar(id, request.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record CategoriaRequest(String nome, String descricao) {
        private Categoria toModel() {
            Categoria categoria = new Categoria();
            categoria.setNome(nome);
            categoria.setDescricao(descricao);
            return categoria;
        }
    }

    private record CategoriaResponse(Long id, String nome, String descricao) {
        private static CategoriaResponse from(Categoria categoria) {
            return new CategoriaResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao());
        }
    }
}
