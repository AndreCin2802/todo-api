package com.example.todo.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.todo.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Component
public class JWTService {

    public static final String TOKEN_EXPIRATION = "86400000";

    public static final String TOKEN_PWD = "b0712295-40e7-4e93-bc2e-99472bff13a5";

    public String gerarToken(Authentication authentication) {
        User logado = (User) authentication.getPrincipal();
        Date hoje = new Date();
        Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(TOKEN_EXPIRATION));

        return Jwts.builder()
                .setIssuer("API Todo List")
                .setSubject(logado.getId().toString())
                .setIssuedAt(hoje)
                .setExpiration(dataExpiracao)
                .signWith(SignatureAlgorithm.HS256, TOKEN_PWD)
                .compact();

    }

    public boolean isTokenValido(String token) {

        try {
            Jwts.parser().setSigningKey(TOKEN_PWD).parseClaimsJws(token);
            return true;

        } catch (Exception e) {return false;}
    }

    public UUID getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(TOKEN_PWD).parseClaimsJws(token).getBody();
        return UUID.fromString(claims.getSubject());
    }

}
