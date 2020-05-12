package vn.ntduycs.javaintern.exceptions;

import org.springframework.web.context.request.WebRequest;

public interface BaseExceptionHandler {

    default String getRequestPath(WebRequest request) {
        return request.getDescription(false).substring(4); // ignore the context path "/api"
    }

    default String getExceptionClassname(Exception e) {
        return e.getClass().getName();
    }

}
