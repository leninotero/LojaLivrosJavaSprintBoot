package com.loja_livros.lojalivros.services.infra.security;

import com.loja_livros.lojalivros.models.UserModel;
import com.loja_livros.lojalivros.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Service
public class SecurityFilterService extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Recover the token from the request header
        // If the token is not present, try to recover it from the Authorization header
        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        // If the token is valid, set the authentication in the security context
        if (login != null) {
            UserModel userModel = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User not found: " + login));
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));   // Define the authorities for the user, you can customize this based on your application's requirements
            var authentication = new UsernamePasswordAuthenticationToken(userModel, null, authorities); // Create an authentication token with the user details and authorities
            SecurityContextHolder.getContext().setAuthentication(authentication);   // Set the authentication in the security context
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
