package com.ProjetoFinal.ecommerce.controller;

import com.ProjetoFinal.ecommerce.config.security.JwtUtil;
import com.ProjetoFinal.ecommerce.dto.LoginRequest;
import com.ProjetoFinal.ecommerce.dto.LoginResponse;
import com.ProjetoFinal.ecommerce.dto.RegisterClienteRequest;
import com.ProjetoFinal.ecommerce.dto.RegisterVendedorRequest;
import com.ProjetoFinal.ecommerce.model.usuario.Cliente;
import com.ProjetoFinal.ecommerce.model.usuario.TipoUsuario;
import com.ProjetoFinal.ecommerce.model.usuario.Usuario;
import com.ProjetoFinal.ecommerce.model.usuario.Vendedor;
import com.ProjetoFinal.ecommerce.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        Usuario usuario = (Usuario) auth.getPrincipal();
        String token = jwtUtil.gerarToken(usuario);
        return ResponseEntity.ok(new LoginResponse(
                token,
                usuario.getTipo().name(),
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNome()
        ));
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<LoginResponse> registrarCliente(@RequestBody RegisterClienteRequest request) {
        validarEmailECpf(request.getEmail(), limparCpf(request.getCpf()));

        Cliente cliente = new Cliente();
        preencherDadosBase(cliente, request.getNome(), request.getEmail(),
                request.getSenha(), limparCpf(request.getCpf()), request.getTelefone(), TipoUsuario.CLIENTE);

        usuarioRepository.save(cliente);
        String token = jwtUtil.gerarToken(cliente);
        return ResponseEntity
                .created(URI.create("/api/clientes/" + cliente.getId()))
                .body(new LoginResponse(token, "CLIENTE", cliente.getId(), cliente.getEmail(), cliente.getNome()));
    }

    @PostMapping("/register/vendedor")
    public ResponseEntity<LoginResponse> registrarVendedor(@RequestBody RegisterVendedorRequest request) {
        validarEmailECpf(request.getEmail(), limparCpf(request.getCpf()));

        Vendedor vendedor = new Vendedor();
        preencherDadosBase(vendedor, request.getNome(), request.getEmail(),
                request.getSenha(), limparCpf(request.getCpf()), request.getTelefone(), TipoUsuario.VENDEDOR);
        vendedor.setNomeLoja(request.getNomeLoja());
        vendedor.setAvaliacao(0.0);

        usuarioRepository.save(vendedor);
        String token = jwtUtil.gerarToken(vendedor);
        return ResponseEntity
                .created(URI.create("/api/vendedores/" + vendedor.getId()))
                .body(new LoginResponse(token, "VENDEDOR", vendedor.getId(), vendedor.getEmail(), vendedor.getNome()));
    }

    private void validarEmailECpf(String email, String cpf) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email já cadastrado: " + email);
        }
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado: " + cpf);
        }
    }

    private String limparCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("[^0-9]", "");
    }

    private void preencherDadosBase(Usuario usuario, String nome, String email,
                                     String senha, String cpf, String telefone, TipoUsuario tipo) {
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setCpf(cpf);
        usuario.setTelefone(telefone);
        usuario.setTipo(tipo);
    }
}
