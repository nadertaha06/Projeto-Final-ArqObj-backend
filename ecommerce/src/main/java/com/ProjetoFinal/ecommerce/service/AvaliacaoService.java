package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import com.ProjetoFinal.ecommerce.model.produto.Avaliacao;
import com.ProjetoFinal.ecommerce.repository.AvaliacaoRepository;
import com.ProjetoFinal.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PedidoRepository pedidoRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, PedidoRepository pedidoRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Avaliacao criar(Avaliacao avaliacao, Long pedidoId) {
        var pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado: " + pedidoId));

        if (pedido.getStatus() != StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Avaliação permitida apenas para pedidos com status ENTREGUE");
        }

        boolean clienteComprou = pedido.getCliente().getId().equals(avaliacao.getCliente().getId());
        if (!clienteComprou) {
            throw new IllegalStateException("O cliente não é o comprador deste pedido");
        }

        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> listarPorProduto(Long produtoId) {
        return avaliacaoRepository.findByProdutoId(produtoId);
    }

    public List<Avaliacao> listarPorCliente(Long clienteId) {
        return avaliacaoRepository.findByClienteId(clienteId);
    }

    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Avaliação não encontrada: " + id));
    }

    public Double calcularMediaPorProduto(Long produtoId) {
        Double media = avaliacaoRepository.calcularMediaNotasPorProduto(produtoId);
        return media != null ? media : 0.0;
    }

    public void deletar(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new NoSuchElementException("Avaliação não encontrada: " + id);
        }
        avaliacaoRepository.deleteById(id);
    }
}
