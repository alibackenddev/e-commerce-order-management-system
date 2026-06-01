package uz.pdp.security_config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenGenerator {
    public static final String SECRET_KEY = "1b51b15ca8c75d96481a456e9171c1bcc31bf3fe1547ed846b589bcb20a4853e";

    public String generateJwtToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(signKey())
                .issuer("junior_java_developer")
                .compact();
    }

    private SecretKey signKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public boolean isValidToken(String token) {

        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date(System.currentTimeMillis()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String username(String token){
        return getClaims(token).getSubject();
    }
}
