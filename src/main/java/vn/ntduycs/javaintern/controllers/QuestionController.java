package vn.ntduycs.javaintern.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import vn.ntduycs.javaintern.commons.WebResponse;
import vn.ntduycs.javaintern.payloads.ChoiceRequest;
import vn.ntduycs.javaintern.payloads.QuestionRequest;
import vn.ntduycs.javaintern.payloads.QuestionResponse;
import vn.ntduycs.javaintern.payloads.VoteRequest;
import vn.ntduycs.javaintern.services.QuestionService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/questions")
public class QuestionController extends BaseController {

    private final QuestionService service;

    public QuestionController(QuestionService questionService) {
        this.service = questionService;
    }

    @PostMapping(path = "")
    public ResponseEntity<?> store(@RequestBody @Valid QuestionRequest request) {
        Long questionId = service.store(request);

        return ResponseEntity.created(getResourceLocation(questionId, "questions")).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long id) {
        QuestionResponse questionContainer = service.show(id);

        return ResponseEntity.ok(WebResponse.build(questionContainer, HttpStatus.OK));
    }

    @GetMapping(path = "")
    public ResponseEntity<?> index(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size,
                                   @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
                                   @RequestParam(name = "direction", defaultValue = "desc") Sort.Direction direction,
                                   @RequestParam(name = "tag", required = false) String tag) {
        Sort sortingConfig = sortBy != null && direction != null
                ? Sort.by(direction, sortBy)
                : Sort.unsorted();

        Pageable pageable = PageRequest.of(page, size, sortingConfig);

        Map<String, Object> questions = StringUtils.hasText(tag)
                ? service.getByTagName(pageable, tag)
                : service.index(pageable);

        return ResponseEntity.ok(WebResponse.build(questions, HttpStatus.OK));
    }

    @PostMapping(path = "/{id}/choices")
    public ResponseEntity<?> addChoice(@PathVariable("id") Long questionId,
                                       @RequestBody @Valid ChoiceRequest choice) {
        service.addChoice(questionId, choice);

        return ResponseEntity.created(getResourceLocation(questionId, "questions")).build();
    }

    @PostMapping(path = "/{id}/votes")
    public ResponseEntity<?> castVote(@PathVariable("id") Long questionId,
                                     @RequestBody @Valid VoteRequest vote) {
        service.castVote(questionId, vote);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> destroy(@PathVariable("id") Long id) {
        service.softRemove(id);

        return ResponseEntity.noContent().build();
    }
}
