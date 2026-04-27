package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.pagamento.Pagamento;
import com.ProjetoFinal.ecommerce.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pagamento> processar(@PathVariable Long pedidoId, @RequestBody Pagamento pagamento) {
        Pagamento processado = pagamentoService.processar(pedidoId, pagamento);
        return ResponseEntity.created(URI.create("/api/pagamentos/" + processado.getId())).body(processado);
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTodos() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
