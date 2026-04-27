package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.promocao.Cupom;
import com.ProjetoFinal.ecommerce.service.CupomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @PostMapping
    public ResponseEntity<CupomResponse> criar(@RequestBody CupomRequest request) {
        Cupom criado = cupomService.criar(request.toModel(), request.produtoId(), request.vendedorId());
        return ResponseEntity.created(URI.create("/api/cupons/" + criado.getId())).body(CupomResponse.from(criado));
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<CupomResponse>> listarPorVendedor(@PathVariable Long vendedorId) {
        return ResponseEntity.ok(cupomService.listarPorVendedor(vendedorId).stream().map(CupomResponse::from).toList());
    }

    @GetMapping
    public ResponseEntity<List<CupomResponse>> listarTodos() {
        return ResponseEntity.ok(cupomService.listarTodos().stream().map(CupomResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CupomResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(CupomResponse.from(cupomService.buscarPorId(id)));
    }

    @PostMapping("/validar")
    public ResponseEntity<CupomResponse> validar(@RequestParam String codigo, @RequestParam Long produtoId) {
        return ResponseEntity.ok(CupomResponse.from(cupomService.validar(codigo, produtoId)));
    }

    @PostMapping("/consumir")
    public ResponseEntity<CupomResponse> consumir(@RequestParam String codigo, @RequestParam Long produtoId) {
        return ResponseEntity.ok(CupomResponse.from(cupomService.consumir(codigo, produtoId)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CupomResponse> atualizar(@PathVariable Long id, @RequestBody CupomRequest request) {
        return ResponseEntity.ok(CupomResponse.from(cupomService.atualizar(id, request.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cupomService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record CupomRequest(
            String codigo,
            BigDecimal desconto,
            LocalDate dataValidade,
            Integer usoMaximo,
            Integer usoAtual,
            Boolean ativo,
            Long produtoId,
            Long vendedorId
    ) {
        private Cupom toModel() {
            Cupom cupom = new Cupom();
            cupom.setCodigo(codigo);
            cupom.setDesconto(desconto);
            cupom.setDataValidade(dataValidade);
            cupom.setUsoMaximo(usoMaximo);
            cupom.setUsoAtual(usoAtual);
            cupom.setAtivo(ativo);
            return cupom;
        }
    }

    private record CupomResponse(
            Long id,
            String codigo,
            BigDecimal desconto,
            LocalDate dataValidade,
            Integer usoMaximo,
            Integer usoAtual,
            Boolean ativo,
            Long produtoId
    ) {
        private static CupomResponse from(Cupom cupom) {
            return new CupomResponse(
                    cupom.getId(),
                    cupom.getCodigo(),
                    cupom.getDesconto(),
                    cupom.getDataValidade(),
                    cupom.getUsoMaximo(),
                    cupom.getUsoAtual(),
                    cupom.getAtivo(),
                    cupom.getProduto().getId()
            );
        }
    }
}
