package at.ac.fhstp.awp_bad.groupxx.auth;

import at.ac.fhstp.awp_bad.groupxx.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component
public class TokenUtil {

    @Value("${values.jwtKey}")
    private String KEY;

    public String generateToken(User user){
        SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
        return Jwts.builder()
                .subject(user.getMail())
                .issuedAt(new Date())
                .claim("name", user.getName())
                .claim("id", user.getId())
                .signWith(key)
                .compact();
    }

    public Optional<User> parseToken(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(KEY.getBytes());
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt);
        }catch (Exception e){
            return Optional.empty();
        }

        User user = new User();
        user.setMail(claims.getPayload().getSubject());
        user.setName(claims.getPayload().get("name", String.class));
        user.setId(claims.getPayload().get("id", Long.class));

        return Optional.of(user);

    }
}
