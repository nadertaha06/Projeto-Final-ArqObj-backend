package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.usuario.Usuario;
import com.ProjetoFinal.ecommerce.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos().stream().map(UsuarioResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.buscarPorId(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record UsuarioResponse(
            Long id,
            String nome,
            String email,
            String cpf,
            String telefone,
            String tipo,
            LocalDateTime dataCadastro
    ) {
        private static UsuarioResponse from(Usuario usuario) {
            return new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getCpf(),
                    usuario.getTelefone(),
                    usuario.getTipo() == null ? null : usuario.getTipo().name(),
                    usuario.getDataCadastro()
            );
        }
    }
}
