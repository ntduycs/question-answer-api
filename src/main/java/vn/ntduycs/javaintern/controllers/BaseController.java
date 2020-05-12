package vn.ntduycs.javaintern.controllers;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public abstract class BaseController {

    protected URI getResourceLocation(Long resourceId, String contextPath) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/" + contextPath + "/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

}
