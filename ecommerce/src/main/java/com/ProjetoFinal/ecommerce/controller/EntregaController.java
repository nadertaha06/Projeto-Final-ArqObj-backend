package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.entrega.Entrega;
import com.ProjetoFinal.ecommerce.model.usuario.Endereco;
import com.ProjetoFinal.ecommerce.service.EntregaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/entregas")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<EntregaResponse> criar(@PathVariable Long pedidoId, @RequestBody EntregaRequest request) {
        Entrega criada = entregaService.criar(pedidoId, request.toModel());
        return ResponseEntity.created(URI.create("/api/entregas/" + criada.getId())).body(EntregaResponse.from(criada));
    }

    @GetMapping
    public ResponseEntity<List<EntregaResponse>> listarTodas() {
        return ResponseEntity.ok(entregaService.listarTodas().stream().map(EntregaResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntregaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(EntregaResponse.from(entregaService.buscarPorId(id)));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<EntregaResponse> buscarPorPedido(@PathVariable Long pedidoId) {
        return entregaService.buscarPorPedidoOptional(pedidoId)
                .map(EntregaResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EntregaResponse> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(EntregaResponse.from(entregaService.atualizarStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        entregaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record EntregaRequest(
            EnderecoRef enderecoDestino,
            String codigoRastreio,
            LocalDate previsaoEntrega
    ) {
        private Entrega toModel() {
            Entrega entrega = new Entrega();
            if (enderecoDestino != null && enderecoDestino.id != null) {
                Endereco endereco = new Endereco();
                endereco.setId(enderecoDestino.id);
                entrega.setEnderecoDestino(endereco);
            }
            entrega.setCodigoRastreio(codigoRastreio);
            entrega.setPrevisaoEntrega(previsaoEntrega);
            return entrega;
        }
    }

    private record EnderecoRef(Long id) {}

    private record EntregaResponse(
            Long id,
            String status,
            String codigoRastreio,
            LocalDate previsaoEntrega,
            LocalDateTime dataEntrega,
            EnderecoResponse enderecoDestino
    ) {
        private static EntregaResponse from(Entrega entrega) {
            return new EntregaResponse(
                    entrega.getId(),
                    entrega.getStatus(),
                    entrega.getCodigoRastreio(),
                    entrega.getPrevisaoEntrega(),
                    entrega.getDataEntrega(),
                    EnderecoResponse.from(entrega.getEnderecoDestino())
            );
        }
    }

    private record EnderecoResponse(
            Long id,
            String rua,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {
        private static EnderecoResponse from(Endereco endereco) {
            if (endereco == null) return null;
            return new EnderecoResponse(
                    endereco.getId(),
                    endereco.getRua(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCep()
            );
        }
    }
}
