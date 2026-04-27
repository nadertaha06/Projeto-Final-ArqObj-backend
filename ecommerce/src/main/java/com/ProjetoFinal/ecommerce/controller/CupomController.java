package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.promocao.Cupom;
import com.ProjetoFinal.ecommerce.service.CupomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @PostMapping
    public ResponseEntity<Cupom> criar(@RequestBody Cupom cupom) {
        Cupom criado = cupomService.criar(cupom);
        return ResponseEntity.created(URI.create("/api/cupons/" + criado.getId())).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Cupom>> listarTodos() {
        return ResponseEntity.ok(cupomService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cupom> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cupomService.buscarPorId(id));
    }

    @PostMapping("/validar")
    public ResponseEntity<Cupom> validarEAplicar(@RequestParam String codigo) {
        return ResponseEntity.ok(cupomService.validarEAplicar(codigo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cupom> atualizar(@PathVariable Long id, @RequestBody Cupom cupom) {
        return ResponseEntity.ok(cupomService.atualizar(id, cupom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cupomService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
