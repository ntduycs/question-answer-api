package vn.ntduycs.javaintern.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ntduycs.javaintern.commons.WebResponse;
import vn.ntduycs.javaintern.payloads.LoginRequest;
import vn.ntduycs.javaintern.services.AuthService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
@Log4j2
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        final Map<String, Object> tokenContainer = authService.handleLogin(request);

        return ResponseEntity.ok(WebResponse.build(tokenContainer, HttpStatus.OK));
    }
}
