package com.ProjetoFinal.ecommerce.dto;

import lombok.Data;

@Data
public class RegisterClienteRequest {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
}
