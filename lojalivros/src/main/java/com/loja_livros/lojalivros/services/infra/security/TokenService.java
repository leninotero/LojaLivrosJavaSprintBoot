package com.loja_livros.lojalivros.services.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.loja_livros.lojalivros.models.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${jwt.tocken.secret}")
    private String secretKey;

    public String generateToken(UserModel userModel) {

        try{
            // Create a JWT token using the user's email as the subject
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            String token = JWT.create()
                    .withIssuer("lojalivros")
                    .withSubject(userModel.getEmail())
                    .withExpiresAt(this.generateExpirationTime())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error while authenticating: " + e.getMessage());
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("lojalivros")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public Instant generateExpirationTime() {
        // Assuming the token expires in 1 hour, you can adjust this as needed
        return LocalDateTime.now().plusMinutes(300000).toInstant(ZoneOffset.of("-03:00"));
    }
}
