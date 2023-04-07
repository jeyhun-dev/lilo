package az.cmammad.security.jwt;

import az.cmammad.common.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.secret-key")
    private String secretKey;

    @Value("${spring.issuer-value}")
    private String issuer;

    public String generateAccessToken(final UserDto userDto) {
        final Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put("username", userDto.getUsername());
        claimsMap.put("full_name", userDto.getFullName());
        claimsMap.put("role", userDto.getAuthorities().toString());
        claimsMap.put("address", userDto.getAddress());
        claimsMap.put("birthday", userDto.getBirthday());
        return createToken(claimsMap, userDto.getUsername());
    }

    private String createToken(final Map<String, String> claimsMap,
                               final String subject) {
        return Jwts
                .builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 + 60 * 24)) //hard code
                .setClaims(claimsMap)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .setAudience(httpServletRequest.getRemoteHost())
                .setIssuer(issuer)
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

    private Key getSignInKey() {
        final byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
