package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.entrega.Entrega;
import com.ProjetoFinal.ecommerce.service.EntregaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> criar(@PathVariable Long pedidoId, @RequestBody Entrega entrega) {
        Entrega criada = entregaService.criar(pedidoId, entrega);
        return ResponseEntity.created(URI.create("/api/entregas/" + criada.getId())).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<Entrega>> listarTodas() {
        return ResponseEntity.ok(entregaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(entregaService.buscarPorId(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Entrega> buscarPorPedido(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(entregaService.buscarPorPedido(pedidoId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Entrega> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(entregaService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        entregaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
