package az.cmammad.security.jwt;

import az.cmammad.common.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final HttpServletRequest httpServletRequest;

    public String generateAccessToken(final UserEntity user) {
        final Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put("username", user.getUsername());
        claimsMap.put("full_name", user.getFullName());
        claimsMap.put("address", user.getAddress());
        claimsMap.put("user_status", user.getUserStatus().name());
        claimsMap.put("create_date", user.getCreatedDate().toString());
        return createToken(claimsMap, user.getUsername());
    }

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

    public boolean validToken(final String token,
                              final UserDetails userDetails) {
        final var username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isExpireToken(token));
    }

    private boolean isExpireToken(final String token) {
        final var expireToken = extractClaim(token, Claims::getExpiration);
        return expireToken.before(new Date());
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
