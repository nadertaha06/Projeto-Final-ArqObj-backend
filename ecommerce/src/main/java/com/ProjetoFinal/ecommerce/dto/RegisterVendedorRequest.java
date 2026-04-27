package com.ProjetoFinal.ecommerce.dto;

import lombok.Data;

@Data
public class RegisterVendedorRequest {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
    private String nomeLoja;
}
