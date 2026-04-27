package com.ProjetoFinal.ecommerce.dto;

import com.ProjetoFinal.ecommerce.model.pagamento.Pagamento;
import com.ProjetoFinal.ecommerce.model.pagamento.PagamentoBoleto;
import com.ProjetoFinal.ecommerce.model.pagamento.PagamentoCartao;
import com.ProjetoFinal.ecommerce.model.pagamento.PagamentoPix;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponse(
        Long id,
        BigDecimal valor,
        LocalDateTime dataPagamento,
        String status,
        String tipo,
        String chavePix,
        String ultimosQuatroDigitos,
        String nomeTitular,
        Integer parcelas
) {
    public static PagamentoResponse from(Pagamento pagamento) {
        if (pagamento instanceof PagamentoPix pix) {
            return new PagamentoResponse(
                    pix.getId(),
                    pix.getValor(),
                    pix.getDataPagamento(),
                    pix.getStatus(),
                    "PagamentoPix",
                    pix.getChavePix(),
                    null,
                    null,
                    null
            );
        }

        if (pagamento instanceof PagamentoCartao cartao) {
            return new PagamentoResponse(
                    cartao.getId(),
                    cartao.getValor(),
                    cartao.getDataPagamento(),
                    cartao.getStatus(),
                    "PagamentoCartao",
                    null,
                    cartao.getUltimosQuatroDigitos(),
                    cartao.getNomeTitular(),
                    cartao.getParcelas()
            );
        }

        if (pagamento instanceof PagamentoBoleto boleto) {
            return new PagamentoResponse(
                    boleto.getId(),
                    boleto.getValor(),
                    boleto.getDataPagamento(),
                    boleto.getStatus(),
                    "PagamentoBoleto",
                    null,
                    null,
                    null,
                    null
            );
        }

        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                "Pagamento",
                null,
                null,
                null,
                null
        );
    }
}
