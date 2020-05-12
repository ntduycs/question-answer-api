package vn.ntduycs.javaintern.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.ntduycs.javaintern.models.User;
import vn.ntduycs.javaintern.payloads.BaseRequest;
import vn.ntduycs.javaintern.payloads.RegisterRequest;

public interface UserService {
    Long store(RegisterRequest request);

    User show(Long id);

    Page<User> index(Pageable pageable);

    void update(Long id, BaseRequest request);

    void destroy(Long id);
}
