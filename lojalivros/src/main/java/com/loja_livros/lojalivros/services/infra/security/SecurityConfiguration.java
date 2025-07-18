package com.loja_livros.lojalivros.services.infra.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private SecurityFilterService securityFilterService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity, consider enabling it in production
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST,"/api/bookstore/auth/login", "/api/bookstore/auth/register").permitAll() // Allow public access to login and register endpoints
                .requestMatchers(HttpMethod.GET, "/api/bookstore/authors", "/api/bookstore/authors/{id}").permitAll() // Allow public access to specific GET endpoints,
                .requestMatchers(HttpMethod.GET, "/api/bookstore/books", "/api/bookstore/books/{id}").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/bookstore/publishers", "/api/bookstore/publishers/{id}").permitAll()
                .anyRequest().authenticated() // Require authentication for all other requests
            )
            .addFilterBefore(securityFilterService, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
