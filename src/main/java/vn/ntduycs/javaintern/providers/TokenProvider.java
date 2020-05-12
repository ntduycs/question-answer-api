package vn.ntduycs.javaintern.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import vn.ntduycs.javaintern.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Service
@Log4j2
public class TokenProvider {

    @Value("${halocom.jwt.secret}")
    private String tokenSecret;

    @Value("${halocom.jwt.duration}")
    private Long tokenDuration;

    public String generateJwtToken(UserDetails user) {
        LocalDateTime now = LocalDateTime.now();

        log.warn("Generating JWT token for user: " + user.getUsername());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(DateTimeUtils.asDate(now))
                .setExpiration(DateTimeUtils.asDate(now.plus(tokenDuration, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    public boolean isJwtTokenValid(final String token, UserDetails user) {
        final String email = this.getEmailFromJwtToken(token);

        return email.equals(user.getUsername()) && !isJwtTokenExpired(token);
    }

    public String getEmailFromJwtToken(final String token) {
        return this.getClaimsFromJwtToken(token, Claims::getSubject);
    }

    public LocalDateTime getExpiryDateOfJwtToken(final String token) {
        return DateTimeUtils.asLocalDateTime(this.getClaimsFromJwtToken(token, Claims::getExpiration));
    }

    private <T> T getClaimsFromJwtToken(final String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();

        return resolver.apply(claims);
    }

    private boolean isJwtTokenExpired(final String token) {
        return this.getExpiryDateOfJwtToken(token).isBefore(LocalDateTime.now());
    }

}
