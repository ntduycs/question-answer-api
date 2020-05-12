package vn.ntduycs.javaintern.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.ntduycs.javaintern.providers.TokenProvider;
import vn.ntduycs.javaintern.payloads.LoginRequest;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Map<String, Object> handleLogin(LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final String token = tokenProvider.generateJwtToken((UserDetails) authentication.getPrincipal());
        final String tokenType = "Bearer";
        final String tokenExpiryDate = tokenProvider
                .getExpiryDateOfJwtToken(token)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"));

        return Map.of(
                "token", token,
                "tokenType", tokenType,
                "expiration", tokenExpiryDate
        );
    }
}
