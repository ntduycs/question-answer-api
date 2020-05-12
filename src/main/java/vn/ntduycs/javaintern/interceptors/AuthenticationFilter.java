package vn.ntduycs.javaintern.interceptors;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.ntduycs.javaintern.providers.AuthenticationProvider;
import vn.ntduycs.javaintern.providers.TokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
@Log4j2
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public AuthenticationFilter(TokenProvider tokenProvider, AuthenticationProvider authenticationProvider) {
        this.tokenProvider = tokenProvider;
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        if (request.getMethod().equalsIgnoreCase("post")) {
            return request.getRequestURI().contains("/auth/login") || request.getRequestURI().contains("/users");
        }

        return false;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromHttpRequest(request);

        if (StringUtils.hasText(token)) {
            try {
                String email = tokenProvider.getEmailFromJwtToken(token);

                UserDetails userDetails = authenticationProvider.loadUserByUsername(email);

                if (tokenProvider.isJwtTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                log.warn("\n\nCould not set user authentication in the security context - Reason: " + e.getMessage() + "\n");
            }
        } else {
            log.warn("\n\nCould not set user authentication in the security context - Reason: Token not found\n");
        }

        filterChain.doFilter(request, response);
    }


    private String getTokenFromHttpRequest(final HttpServletRequest request) {
        String auth = request.getHeader("Authorization");

        if (StringUtils.hasText(auth) && auth.startsWith(TOKEN_PREFIX)) {
            return auth.substring(TOKEN_PREFIX.length());
        }

        return null;
    }
}
