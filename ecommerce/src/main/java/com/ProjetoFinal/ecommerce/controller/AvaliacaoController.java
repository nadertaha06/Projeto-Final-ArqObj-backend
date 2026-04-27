package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.produto.Avaliacao;
import com.ProjetoFinal.ecommerce.service.AvaliacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponse> criar(
            @RequestBody Avaliacao avaliacao,
            @RequestParam Long pedidoId
    ) {
        Avaliacao criada = avaliacaoService.criar(avaliacao, pedidoId);
        return ResponseEntity
                .created(URI.create("/api/avaliacoes/" + criada.getId()))
                .body(AvaliacaoResponse.from(criada));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<AvaliacaoResponse>> listarPorProduto(@PathVariable Long produtoId) {
        List<AvaliacaoResponse> resposta = avaliacaoService.listarPorProduto(produtoId)
                .stream()
                .map(AvaliacaoResponse::from)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AvaliacaoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<AvaliacaoResponse> resposta = avaliacaoService.listarPorCliente(clienteId)
                .stream()
                .map(AvaliacaoResponse::from)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(AvaliacaoResponse.from(avaliacaoService.buscarPorId(id)));
    }

    @GetMapping("/produto/{produtoId}/media")
    public ResponseEntity<Double> calcularMedia(@PathVariable Long produtoId) {
        return ResponseEntity.ok(avaliacaoService.calcularMediaPorProduto(produtoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record AvaliacaoResponse(
            Long id,
            Integer nota,
            String comentario,
            LocalDateTime dataCriacao,
            Long clienteId
    ) {
        private static AvaliacaoResponse from(Avaliacao avaliacao) {
            return new AvaliacaoResponse(
                    avaliacao.getId(),
                    avaliacao.getNota(),
                    avaliacao.getComentario(),
                    avaliacao.getDataCriacao(),
                    avaliacao.getCliente() == null ? null : avaliacao.getCliente().getId()
            );
        }
    }
}
