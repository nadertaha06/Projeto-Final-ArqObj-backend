package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.pedido.ItemPedido;
import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import com.ProjetoFinal.ecommerce.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<PedidoResponse> criarDesdeCarrinho(
            @PathVariable Long clienteId,
            @RequestBody(required = false) CriarPedidoRequest request
    ) {
        Pedido pedido = pedidoService.criarDesdoCarrinho(
                clienteId,
                request == null ? null : request.toEndereco()
        );
        return ResponseEntity
                .created(URI.create("/api/pedidos/" + pedido.getId()))
                .body(PedidoResponse.from(pedido));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<PedidoResponse> resposta = pedidoService.listarTodos()
                .stream()
                .map(PedidoResponse::from)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoResponse.from(pedidoService.buscarPorId(id)));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponse> resposta = pedidoService.listarPorCliente(clienteId)
                .stream()
                .map(PedidoResponse::from)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<PedidoResponse>> listarPorVendedor(@PathVariable Long vendedorId) {
        List<PedidoResponse> resposta = pedidoService.listarPorVendedor(vendedorId)
                .stream()
                .map(PedidoResponse::from)
                .toList();
        return ResponseEntity.ok(resposta);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        return ResponseEntity.ok(PedidoResponse.from(pedidoService.atualizarStatus(id, status)));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(PedidoResponse.from(pedidoService.cancelar(id)));
    }

    private record PedidoResponse(
            Long id,
            StatusPedido status,
            BigDecimal valorTotal,
            LocalDateTime dataCriacao,
            ClientePedidoResponse cliente,
            EntregaPedidoResponse entrega,
            List<ItemPedidoResponse> itens
    ) {
        private static PedidoResponse from(Pedido pedido) {
            List<ItemPedidoResponse> itens = pedido.getItens()
                    .stream()
                    .map(ItemPedidoResponse::from)
                    .toList();
            return new PedidoResponse(
                    pedido.getId(),
                    pedido.getStatus(),
                    pedido.getValorTotal(),
                    pedido.getDataCriacao(),
                    ClientePedidoResponse.from(pedido),
                    EntregaPedidoResponse.from(pedido),
                    itens
            );
        }
    }

    private record ItemPedidoResponse(
            Long id,
            Integer quantidade,
            BigDecimal precoUnitario,
            ProdutoPedidoResponse produto
    ) {
        private static ItemPedidoResponse from(ItemPedido item) {
            return new ItemPedidoResponse(
                    item.getId(),
                    item.getQuantidade(),
                    item.getPrecoUnitario(),
                    ProdutoPedidoResponse.from(item)
            );
        }
    }

    private record ProdutoPedidoResponse(
            Long id,
            String nome,
            BigDecimal preco,
            String imagemUrl
    ) {
        private static ProdutoPedidoResponse from(ItemPedido item) {
            var produto = item.getProduto();
            return new ProdutoPedidoResponse(
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getImagemUrl()
            );
        }
    }

    private record ClientePedidoResponse(Long id, String nome) {
        private static ClientePedidoResponse from(Pedido pedido) {
            if (pedido.getCliente() == null) return null;
            return new ClientePedidoResponse(
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome()
            );
        }
    }

    private record EntregaPedidoResponse(
            Long id,
            String status,
            String codigoRastreio,
            String previsaoEntrega,
            EnderecoPedidoResponse endereco
    ) {
        private static EntregaPedidoResponse from(Pedido pedido) {
            if (pedido.getEntrega() == null) return null;
            var entrega = pedido.getEntrega();
            return new EntregaPedidoResponse(
                    entrega.getId(),
                    entrega.getStatus(),
                    entrega.getCodigoRastreio(),
                    entrega.getPrevisaoEntrega() == null ? null : entrega.getPrevisaoEntrega().toString(),
                    EnderecoPedidoResponse.from(entrega.getEnderecoDestino())
            );
        }
    }

    private record EnderecoPedidoResponse(
            String rua,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {
        private static EnderecoPedidoResponse from(com.ProjetoFinal.ecommerce.model.usuario.Endereco endereco) {
            if (endereco == null) return null;
            return new EnderecoPedidoResponse(
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

    private record CriarPedidoRequest(EnderecoRequest enderecoEntrega) {
        private com.ProjetoFinal.ecommerce.model.usuario.Endereco toEndereco() {
            if (enderecoEntrega == null) return null;
            return enderecoEntrega.toModel();
        }
    }

    private record EnderecoRequest(
            String rua,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {
        private com.ProjetoFinal.ecommerce.model.usuario.Endereco toModel() {
            com.ProjetoFinal.ecommerce.model.usuario.Endereco endereco = new com.ProjetoFinal.ecommerce.model.usuario.Endereco();
            endereco.setRua(rua);
            endereco.setNumero(numero);
            endereco.setComplemento(complemento);
            endereco.setBairro(bairro);
            endereco.setCidade(cidade);
            endereco.setEstado(estado);
            endereco.setCep(cep);
            return endereco;
        }
    }
}
