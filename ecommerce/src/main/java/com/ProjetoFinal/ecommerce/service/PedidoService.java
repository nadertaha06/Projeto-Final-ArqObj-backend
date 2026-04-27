package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.pedido.ItemPedido;
import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import com.ProjetoFinal.ecommerce.model.entrega.Entrega;
import com.ProjetoFinal.ecommerce.model.usuario.Endereco;
import com.ProjetoFinal.ecommerce.model.usuario.Cliente;
import com.ProjetoFinal.ecommerce.repository.EnderecoRepository;
import com.ProjetoFinal.ecommerce.repository.EntregaRepository;
import com.ProjetoFinal.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final EstoqueService estoqueService;
    private final CarrinhoService carrinhoService;
    private final EnderecoRepository enderecoRepository;
    private final EntregaRepository entregaRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteService clienteService,
                         EstoqueService estoqueService,
                         CarrinhoService carrinhoService,
                         EnderecoRepository enderecoRepository,
                         EntregaRepository entregaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.estoqueService = estoqueService;
        this.carrinhoService = carrinhoService;
        this.enderecoRepository = enderecoRepository;
        this.entregaRepository = entregaRepository;
    }

    public Pedido criarDesdoCarrinho(Long clienteId, Endereco enderecoEntregaRequest) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        var carrinho = carrinhoService.buscarPorCliente(clienteId);

        if (carrinho.getItens().isEmpty()) {
            throw new IllegalStateException("Carrinho está vazio");
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .status(StatusPedido.PAGO)
                .build();

        List<ItemPedido> itensPedido = carrinho.getItens().stream().map(itemCarrinho -> {
            estoqueService.reservar(itemCarrinho.getProduto().getId(), itemCarrinho.getQuantidade());
            return ItemPedido.builder()
                    .pedido(pedido)
                    .produto(itemCarrinho.getProduto())
                    .quantidade(itemCarrinho.getQuantidade())
                    .precoUnitario(itemCarrinho.getProduto().getPreco())
                    .build();
        }).toList();

        pedido.setItens(itensPedido);
        pedido.setValorTotal(calcularTotal(itensPedido));

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        Endereco enderecoEntrega = resolverEnderecoEntrega(clienteId, cliente, enderecoEntregaRequest);
        criarEntregaInicial(pedidoSalvo, enderecoEntrega);
        carrinhoService.limpar(clienteId);
        return pedidoRepository.findDetailedById(pedidoSalvo.getId()).orElse(pedidoSalvo);
    }

    private Endereco resolverEnderecoEntrega(Long clienteId, Cliente cliente, Endereco enderecoEntregaRequest) {
        if (enderecoEntregaRequest != null
                && enderecoEntregaRequest.getRua() != null
                && !enderecoEntregaRequest.getRua().isBlank()
                && enderecoEntregaRequest.getNumero() != null
                && !enderecoEntregaRequest.getNumero().isBlank()
                && enderecoEntregaRequest.getBairro() != null
                && !enderecoEntregaRequest.getBairro().isBlank()
                && enderecoEntregaRequest.getCidade() != null
                && !enderecoEntregaRequest.getCidade().isBlank()
                && enderecoEntregaRequest.getEstado() != null
                && !enderecoEntregaRequest.getEstado().isBlank()
                && enderecoEntregaRequest.getCep() != null
                && !enderecoEntregaRequest.getCep().isBlank()) {
            Endereco novoEndereco = Endereco.builder()
                    .rua(enderecoEntregaRequest.getRua())
                    .numero(enderecoEntregaRequest.getNumero())
                    .complemento(enderecoEntregaRequest.getComplemento())
                    .bairro(enderecoEntregaRequest.getBairro())
                    .cidade(enderecoEntregaRequest.getCidade())
                    .estado(enderecoEntregaRequest.getEstado())
                    .cep(enderecoEntregaRequest.getCep())
                    .cliente(cliente)
                    .build();
            return enderecoRepository.save(novoEndereco);
        }

        return enderecoRepository.findFirstByClienteIdOrderByIdAsc(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Informe um endereço de entrega para finalizar o pedido"));
    }

    private void criarEntregaInicial(Pedido pedido, Endereco enderecoEntrega) {
        Entrega entrega = Entrega.builder()
                .pedido(pedido)
                .enderecoDestino(enderecoEntrega)
                .status("AGUARDANDO_COLETA")
                .codigoRastreio("PED-" + pedido.getId())
                .previsaoEntrega(LocalDate.now().plusDays(5))
                .build();
        entregaRepository.save(entrega);
    }

    private BigDecimal calcularTotal(List<ItemPedido> itens) {
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllDetailed();
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findDetailedByClienteId(clienteId);
    }

    public List<Pedido> listarPorVendedor(Long vendedorId) {
        return pedidoRepository.findDetailedByVendedorId(vendedorId);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findDetailedById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado: " + id));
    }

    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
        entregaRepository.findByPedidoId(id).ifPresent(entrega -> {
            if (novoStatus == StatusPedido.ENVIADO) {
                entrega.setStatus("EM_TRANSITO");
                entregaRepository.save(entrega);
            } else if (novoStatus == StatusPedido.ENTREGUE) {
                entrega.setStatus("ENTREGUE");
                entrega.setDataEntrega(LocalDateTime.now());
                entregaRepository.save(entrega);
            }
        });
        return pedidoRepository.save(pedido);
    }

    public Pedido cancelar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getStatus() == StatusPedido.ENVIADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Não é possível cancelar pedido já enviado ou entregue");
        }
        pedido.getItens().forEach(item ->
                estoqueService.devolver(item.getProduto().getId(), item.getQuantidade()));
        pedido.setStatus(StatusPedido.CANCELADO);
        entregaRepository.findByPedidoId(id).ifPresent(entrega -> {
            entrega.setStatus("CANCELADA");
            entregaRepository.save(entrega);
        });
        return pedidoRepository.save(pedido);
    }
}
