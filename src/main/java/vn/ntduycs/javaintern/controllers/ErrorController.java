package vn.ntduycs.javaintern.controllers;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping({"/error"})
public class ErrorController extends AbstractErrorController {

    private static final boolean NOT_INCLUDE_STACKTRACE = false;

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes, Collections.emptyList());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(path = "")
    public ResponseEntity<?> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, NOT_INCLUDE_STACKTRACE);

        HttpStatus status = getStatus(request);

        return new ResponseEntity<>(body, status);
    }
}
