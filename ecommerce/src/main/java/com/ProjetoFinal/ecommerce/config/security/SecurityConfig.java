package com.ProjetoFinal.ecommerce.config.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
            )
            .authorizeHttpRequests(auth -> {
                PathPatternRequestMatcher.Builder m = PathPatternRequestMatcher.withDefaults();
                auth
                    .requestMatchers(m.matcher("/error")).permitAll()
                    .requestMatchers(m.matcher("/api/auth/**")).permitAll()
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/categorias/**")).permitAll()
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/avaliacoes/produto/**")).permitAll()
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/produtos/vendedor/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/produtos/**")).permitAll()
                    .requestMatchers(m.matcher("/api/usuarios/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher("/api/clientes/**")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher("/api/vendedores/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher("/api/carrinho/**")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher(HttpMethod.POST, "/api/pedidos/cliente/**")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/pedidos/cliente/**")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/pedidos/vendedor/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher(HttpMethod.PATCH, "/api/pedidos/**")).authenticated()
                    .requestMatchers(m.matcher(HttpMethod.GET, "/api/entregas/pedido/**")).authenticated()
                    .requestMatchers(m.matcher(HttpMethod.PATCH, "/api/entregas/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher("/api/estoques/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher(HttpMethod.POST, "/api/cupons/validar")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher(HttpMethod.POST, "/api/cupons/consumir")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher("/api/cupons/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher("/api/pagamentos/**")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher(HttpMethod.POST, "/api/avaliacoes/**")).hasRole("CLIENTE")
                    .requestMatchers(m.matcher(HttpMethod.POST, "/api/produtos/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher(HttpMethod.PUT, "/api/produtos/**")).hasRole("VENDEDOR")
                    .requestMatchers(m.matcher(HttpMethod.DELETE, "/api/produtos/**")).hasRole("VENDEDOR")
                    .anyRequest().authenticated();
            })
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
