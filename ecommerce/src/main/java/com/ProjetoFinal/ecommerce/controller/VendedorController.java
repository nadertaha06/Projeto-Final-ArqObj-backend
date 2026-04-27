package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.usuario.Vendedor;
import com.ProjetoFinal.ecommerce.model.usuario.TipoUsuario;
import com.ProjetoFinal.ecommerce.service.VendedorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    private final VendedorService vendedorService;

    public VendedorController(VendedorService vendedorService) {
        this.vendedorService = vendedorService;
    }

    @PostMapping
    public ResponseEntity<VendedorResponse> criar(@RequestBody VendedorRequest request) {
        Vendedor criado = vendedorService.criar(request.toModel());
        return ResponseEntity.created(URI.create("/api/vendedores/" + criado.getId())).body(VendedorResponse.from(criado));
    }

    @GetMapping
    public ResponseEntity<List<VendedorResponse>> listarTodos() {
        return ResponseEntity.ok(vendedorService.listarTodos().stream().map(VendedorResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(VendedorResponse.from(vendedorService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorResponse> atualizar(@PathVariable Long id, @RequestBody VendedorRequest request) {
        return ResponseEntity.ok(VendedorResponse.from(vendedorService.atualizar(id, request.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vendedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record VendedorRequest(
            String nome,
            String email,
            String senha,
            String cpf,
            String telefone,
            String nomeLoja
    ) {
        private Vendedor toModel() {
            Vendedor vendedor = new Vendedor();
            vendedor.setNome(nome);
            vendedor.setEmail(email);
            vendedor.setSenha(senha);
            vendedor.setCpf(cpf);
            vendedor.setTelefone(telefone);
            vendedor.setNomeLoja(nomeLoja);
            vendedor.setTipo(TipoUsuario.VENDEDOR);
            return vendedor;
        }
    }

    private record VendedorResponse(
            Long id,
            String nome,
            String email,
            String cpf,
            String telefone,
            String nomeLoja,
            Double avaliacao,
            LocalDateTime dataCadastro
    ) {
        private static VendedorResponse from(Vendedor vendedor) {
            return new VendedorResponse(
                    vendedor.getId(),
                    vendedor.getNome(),
                    vendedor.getEmail(),
                    vendedor.getCpf(),
                    vendedor.getTelefone(),
                    vendedor.getNomeLoja(),
                    vendedor.getAvaliacao(),
                    vendedor.getDataCadastro()
            );
        }
    }
}
