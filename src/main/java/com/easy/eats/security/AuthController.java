package com.easy.eats.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easy.eats.empresa.service.EmpresaService;
import com.easy.eats.segmento.model.Funcionalidade;
import com.easy.eats.usuario.model.Usuario;
import com.easy.eats.usuario.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final EmpresaService empresaService;

    public AuthController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            UsuarioRepository usuarioRepository,
            JwtService jwtService,
            EmpresaService empresaService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.empresaService = empresaService;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
        } catch (AuthenticationException ex) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String role = usuario.getRole() != null ? usuario.getRole().name() : "OPERADOR";

        String token = jwtService.gerarToken(userDetails, Map.of("role", role, "nome", usuario.getNome()));

        List<String> funcionalidades = empresaService.funcionalidadesHabilitadas(usuario.getEmpresa()).stream()
                .map(Funcionalidade::name)
                .toList();

        return new AuthResponse(token, usuario.getId(), usuario.getNome(), usuario.getEmail(), role, funcionalidades);
    }
}
