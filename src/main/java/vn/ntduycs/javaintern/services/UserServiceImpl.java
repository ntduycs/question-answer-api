package vn.ntduycs.javaintern.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.ntduycs.javaintern.exceptions.ResourceNotFoundException;
import vn.ntduycs.javaintern.models.User;
import vn.ntduycs.javaintern.repositories.UserRepository;
import vn.ntduycs.javaintern.payloads.BaseRequest;
import vn.ntduycs.javaintern.payloads.RegisterRequest;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long store(RegisterRequest request) {
        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getFullName());

        return userRepository.save(user).getId();
    }

    @Override
    public User show(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Override
    public Page<User> index(Pageable pageable) {
        return null;
    }

    @Override
    public void update(Long id, BaseRequest request) {

    }

    @Override
    public void destroy(Long id) {

    }
}
