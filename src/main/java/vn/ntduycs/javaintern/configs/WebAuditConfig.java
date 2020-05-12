package vn.ntduycs.javaintern.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import vn.ntduycs.javaintern.models.User;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class WebAuditConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new AuditorAwareImpl();
    }

    private static class AuditorAwareImpl implements AuditorAware<Long> {

        @Override
        @NonNull
        public Optional<Long> getCurrentAuditor() {
            Object currentUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (currentUser instanceof UserDetails) {
                return Optional.of(((User) currentUser).getId());
            }

            return Optional.empty();
        }
    }

}
