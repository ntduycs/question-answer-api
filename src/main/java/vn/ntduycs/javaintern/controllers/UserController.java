package vn.ntduycs.javaintern.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ntduycs.javaintern.commons.WebResponse;
import vn.ntduycs.javaintern.models.User;
import vn.ntduycs.javaintern.payloads.RegisterRequest;
import vn.ntduycs.javaintern.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@Log4j2
public class UserController extends BaseController {

    private final UserService service;

    @Autowired
    public UserController(UserService userService) {
        this.service = userService;
    }

    @PostMapping(path = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<?> store(@RequestBody @Valid RegisterRequest request) {
        Long userId = service.store(request);

        return ResponseEntity.created(getResourceLocation(userId, "users")).build();
    }

    @GetMapping(path = "")
    public ResponseEntity<?> index(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
                                   @RequestParam(name = "direction", defaultValue = "desc") Sort.Direction direction) {
        Sort sortingConfig = sortBy != null && direction != null
                ? Sort.by(direction, sortBy)
                : Sort.unsorted();

        Pageable pageable = PageRequest.of(page, size, sortingConfig);

        Page<User> users = service.index(pageable);

        return ResponseEntity.ok(WebResponse.build(users, HttpStatus.OK));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        User user = service.show(id);

        return ResponseEntity.ok(WebResponse.build(user, HttpStatus.OK));
    }
}
