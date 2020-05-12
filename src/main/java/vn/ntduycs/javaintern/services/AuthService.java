package vn.ntduycs.javaintern.services;

import vn.ntduycs.javaintern.payloads.LoginRequest;

import java.util.Map;

public interface AuthService {

    Map<String, Object> handleLogin(LoginRequest request);

}
