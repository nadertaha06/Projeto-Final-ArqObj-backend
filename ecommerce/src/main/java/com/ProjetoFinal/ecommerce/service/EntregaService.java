package com.ProjetoFinal.ecommerce.service;

import com.ProjetoFinal.ecommerce.model.entrega.Entrega;
import com.ProjetoFinal.ecommerce.model.pedido.Pedido;
import com.ProjetoFinal.ecommerce.model.pedido.StatusPedido;
import com.ProjetoFinal.ecommerce.repository.EntregaRepository;
import com.ProjetoFinal.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;

    public EntregaService(EntregaRepository entregaRepository, PedidoRepository pedidoRepository) {
        this.entregaRepository = entregaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Entrega criar(Long pedidoId, Entrega entrega) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado: " + pedidoId));

        if (pedido.getStatus() != StatusPedido.PAGO && pedido.getStatus() != StatusPedido.EM_PREPARACAO) {
            throw new IllegalStateException("Entrega só pode ser criada para pedidos pagos ou em preparação");
        }

        entrega.setPedido(pedido);
        entrega.setStatus("AGUARDANDO_COLETA");
        Entrega entregaSalva = entregaRepository.save(entrega);

        pedido.setStatus(StatusPedido.EM_PREPARACAO);
        pedidoRepository.save(pedido);

        return entregaSalva;
    }

    public List<Entrega> listarTodas() {
        return entregaRepository.findAll();
    }

    public Entrega buscarPorId(Long id) {
        return entregaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entrega não encontrada: " + id));
    }

    public Entrega buscarPorPedido(Long pedidoId) {
        return entregaRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Entrega não encontrada para o pedido: " + pedidoId));
    }

    public Entrega atualizarStatus(Long id, String novoStatus) {
        Entrega entrega = buscarPorId(id);
        entrega.setStatus(novoStatus);

        if ("ENTREGUE".equals(novoStatus)) {
            entrega.setDataEntrega(LocalDateTime.now());
            Pedido pedido = entrega.getPedido();
            pedido.setStatus(StatusPedido.ENTREGUE);
            pedidoRepository.save(pedido);
        } else if ("ENVIADO".equals(novoStatus)) {
            Pedido pedido = entrega.getPedido();
            pedido.setStatus(StatusPedido.ENVIADO);
            pedidoRepository.save(pedido);
        }

        return entregaRepository.save(entrega);
    }

    public void deletar(Long id) {
        if (!entregaRepository.existsById(id)) {
            throw new NoSuchElementException("Entrega não encontrada: " + id);
        }
        entregaRepository.deleteById(id);
    }
}
