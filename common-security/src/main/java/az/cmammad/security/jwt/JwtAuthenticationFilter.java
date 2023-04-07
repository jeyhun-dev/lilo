package az.cmammad.security.jwt;

import az.cmammad.common.UserDto;
import az.cmammad.security.constant.SecurityConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String headerAuth = request.getHeader(SecurityConst.AUTHORISATION);
        final String jwt;
        final String username;

        if (headerAuth == null || headerAuth.startsWith(SecurityConst.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = headerAuth.substring(7);
        username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDto userDto = getUserDtoForUserDetails(username);

            if (jwtService.validToken(jwt, userDto)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDto.getUsername(),
                                userDto.getPassword(),
                                userDto.getAuthorities());

                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
        }
    }

    private UserDto getUserDtoForUserDetails(final String username) {
        return new UserDto();
    }
}
