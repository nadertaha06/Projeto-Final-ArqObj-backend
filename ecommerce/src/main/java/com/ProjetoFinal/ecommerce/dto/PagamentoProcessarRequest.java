package com.ProjetoFinal.ecommerce.dto;

public record PagamentoProcessarRequest(
        String tipo,
        String chavePix,
        String ultimosQuatroDigitos,
        String nomeTitular,
        Integer parcelas
) {
}
