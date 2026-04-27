package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.pedido.ItemPedido;
import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import com.ProjetoFinal.ecommerce.model.usuario.Cliente;
import com.ProjetoFinal.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final EstoqueService estoqueService;
    private final CarrinhoService carrinhoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteService clienteService,
                         EstoqueService estoqueService,
                         CarrinhoService carrinhoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.estoqueService = estoqueService;
        this.carrinhoService = carrinhoService;
    }

    public Pedido criarDesdoCarrinho(Long clienteId) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        var carrinho = carrinhoService.buscarPorCliente(clienteId);

        if (carrinho.getItens().isEmpty()) {
            throw new IllegalStateException("Carrinho está vazio");
        }

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .status(StatusPedido.AGUARDANDO_PAGAMENTO)
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
        carrinhoService.limpar(clienteId);
        return pedidoSalvo;
    }

    private BigDecimal calcularTotal(List<ItemPedido> itens) {
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado: " + id));
    }

    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(novoStatus);
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
        return pedidoRepository.save(pedido);
    }
}
