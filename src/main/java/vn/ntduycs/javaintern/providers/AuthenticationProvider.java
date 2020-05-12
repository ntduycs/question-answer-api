package vn.ntduycs.javaintern.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ntduycs.javaintern.models.User;
import vn.ntduycs.javaintern.repositories.UserRepository;

@Service
public class AuthenticationProvider implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            final User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("No user found with email '" + email + "'");
            }

            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
