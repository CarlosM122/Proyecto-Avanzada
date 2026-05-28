package uq.sistemagestionsolicitudes.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import uq.sistemagestionsolicitudes.model.Usuario;

import java.security.Key;
import java.util.Date;
@Service
public class JwtService {
    private final String SECRET_KEY = "mi_clave_super_secreta_12345678901234567890";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Usuario usuario) {

        return Jwts.builder()
                .setSubject(usuario.getCorreo())
                .claim("role",usuario.getRole())
                .claim("nombre",usuario.getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, String username) {

        return extractUsername(token).equals(username);
    }
}