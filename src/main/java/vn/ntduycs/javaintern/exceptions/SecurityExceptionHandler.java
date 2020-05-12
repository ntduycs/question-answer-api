package vn.ntduycs.javaintern.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import vn.ntduycs.javaintern.commons.WebError;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class SecurityExceptionHandler implements BaseExceptionHandler {

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    public ResponseEntity<?> handlerAuthenticationServiceException(InternalAuthenticationServiceException ex, WebRequest request) {
        WebError error = new WebError(
                UNAUTHORIZED.value(),
                UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                getExceptionClassname(new AuthenticationServiceException("")),
                getRequestPath(request)
        );

        return new ResponseEntity<>(error, UNAUTHORIZED);
    }

}
