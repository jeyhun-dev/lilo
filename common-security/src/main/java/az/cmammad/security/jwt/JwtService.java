package az.cmammad.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final HttpServletRequest httpServletRequest;

   /*burada bezi case'leri override ele, hard coded qalmasin*/
    private String createToken(final Map<String, String> claimsMap,
                               final String subject) {
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 + 60 * 24))
                .setClaims(claimsMap)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setAudience(httpServletRequest.getRemoteHost())
                .setIssuer("Jeyhun Mammadsaidov")
                .compact();
    }

    public String extractUsername(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(final String token,
                               final Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /*buna yeniden bax, secret qoy*/
    private Key getSignInKey() {
        final byte[] keyByte = Decoders.BASE64.decode("");
        return Keys.hmacShaKeyFor(keyByte);
    }
}
