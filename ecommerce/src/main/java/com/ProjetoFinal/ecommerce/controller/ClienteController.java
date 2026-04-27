package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.model.usuario.Cliente;
import com.ProjetoFinal.ecommerce.model.usuario.Endereco;
import com.ProjetoFinal.ecommerce.model.usuario.TipoUsuario;
import com.ProjetoFinal.ecommerce.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@RequestBody ClienteRequest request) {
        Cliente criado = clienteService.criar(request.toModel());
        return ResponseEntity.created(URI.create("/api/clientes/" + criado.getId())).body(ClienteResponse.from(criado));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos().stream().map(ClienteResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ClienteResponse.from(clienteService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(ClienteResponse.from(clienteService.atualizar(id, request.toModel())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private record ClienteRequest(
            String nome,
            String email,
            String senha,
            String cpf,
            String telefone
    ) {
        private Cliente toModel() {
            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setSenha(senha);
            cliente.setCpf(cpf);
            cliente.setTelefone(telefone);
            cliente.setTipo(TipoUsuario.CLIENTE);
            return cliente;
        }
    }

    private record ClienteResponse(
            Long id,
            String nome,
            String email,
            String cpf,
            String telefone,
            LocalDateTime dataCadastro,
            List<EnderecoResponse> enderecos
    ) {
        private static ClienteResponse from(Cliente cliente) {
            List<EnderecoResponse> enderecos = cliente.getEnderecos() == null
                    ? List.of()
                    : cliente.getEnderecos().stream().map(EnderecoResponse::from).toList();
            return new ClienteResponse(
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getEmail(),
                    cliente.getCpf(),
                    cliente.getTelefone(),
                    cliente.getDataCadastro(),
                    enderecos
            );
        }
    }

    private record EnderecoResponse(
            Long id,
            String rua,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep
    ) {
        private static EnderecoResponse from(Endereco endereco) {
            return new EnderecoResponse(
                    endereco.getId(),
                    endereco.getRua(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCep()
            );
        }
    }
}
