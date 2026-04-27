package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.dto.PagamentoProcessarRequest;
import com.ProjetoFinal.ecommerce.dto.PagamentoResponse;
import com.ProjetoFinal.ecommerce.model.pagamento.PagamentoBoleto;
import com.ProjetoFinal.ecommerce.model.pagamento.PagamentoCartao;
import com.ProjetoFinal.ecommerce.model.pagamento.PagamentoPix;
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
    public ResponseEntity<PagamentoResponse> processar(
            @PathVariable Long pedidoId,
            @RequestBody PagamentoProcessarRequest request
    ) {
        Pagamento pagamento = toModel(request);
        Pagamento processado = pagamentoService.processar(pedidoId, pagamento);
        return ResponseEntity
                .created(URI.create("/api/pagamentos/" + processado.getId()))
                .body(PagamentoResponse.from(processado));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponse>> listarTodos() {
        List<PagamentoResponse> resposta = pagamentoService.listarTodos()
                .stream()
                .map(PagamentoResponse::from)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PagamentoResponse.from(pagamentoService.buscarPorId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private Pagamento toModel(PagamentoProcessarRequest request) {
        if ("PagamentoPix".equals(request.tipo())) {
            PagamentoPix pix = new PagamentoPix();
            pix.setChavePix(request.chavePix());
            return pix;
        }

        if ("PagamentoCartao".equals(request.tipo())) {
            PagamentoCartao cartao = new PagamentoCartao();
            cartao.setUltimosQuatroDigitos(request.ultimosQuatroDigitos());
            cartao.setNomeTitular(request.nomeTitular());
            cartao.setParcelas(request.parcelas());
            return cartao;
        }

        if ("PagamentoBoleto".equals(request.tipo())) {
            return new PagamentoBoleto();
        }

        throw new IllegalArgumentException("Tipo de pagamento inválido: " + request.tipo());
    }
}
