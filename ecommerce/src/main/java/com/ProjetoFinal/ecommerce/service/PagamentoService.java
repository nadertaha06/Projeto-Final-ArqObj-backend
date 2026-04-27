package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.pagamento.Pagamento;
import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import com.ProjetoFinal.ecommerce.repository.PagamentoRepository;
import com.ProjetoFinal.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, PedidoRepository pedidoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Pagamento processar(Long pedidoId, Pagamento pagamento) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado: " + pedidoId));

        if (pedido.getPagamento() != null) {
            return pedido.getPagamento();
        }

        if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO && pedido.getStatus() != StatusPedido.PAGO) {
            throw new IllegalStateException("Pedido não está apto para processar pagamento");
        }

        pagamento.setValor(pedido.getValorTotal());
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setStatus("APROVADO");

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        pedido.setPagamento(pagamentoSalvo);
        pedido.setStatus(StatusPedido.PAGO);
        pedidoRepository.save(pedido);

        return pagamentoSalvo;
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pagamento não encontrado: " + id));
    }

    public void deletar(Long id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new NoSuchElementException("Pagamento não encontrado: " + id);
        }
        pagamentoRepository.deleteById(id);
    }
}
