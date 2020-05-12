package vn.ntduycs.javaintern.exceptions;

import com.google.common.base.Joiner;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.ntduycs.javaintern.commons.WebError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

@RestControllerAdvice
@Log4j2
public class WebExceptionHandler extends ResponseEntityExceptionHandler implements BaseExceptionHandler {

    private List<WebError.SubError> getSubErrors(BindingResult bindingResult) {
        List<WebError.SubError> errors = new ArrayList<>();

        for (final FieldError error : bindingResult.getFieldErrors()) {
            errors.add(new WebError.SubError(
                    error.getField() + " was given with value " + error.getRejectedValue(),
                    error.getDefaultMessage()
            ));
        }
        for (final ObjectError error : bindingResult.getGlobalErrors()) {
            errors.add(new WebError.SubError(
                    error.getObjectName(),
                    error.getDefaultMessage()
            ));
        }

        return errors;
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        final List<WebError.SubError> errors = getSubErrors(ex.getBindingResult());

        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Method argument(s) not valid",
                getExceptionClassname(ex),
                getRequestPath(request),
                errors
        ));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleBindException(
            BindException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        final List<WebError.SubError> errors = getSubErrors(ex.getBindingResult());

        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Binding error happened",
                getExceptionClassname(ex),
                getRequestPath(request),
                errors
        ));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex,
                                                        @NonNull HttpHeaders headers,
                                                        @NonNull HttpStatus status,
                                                        @NonNull WebRequest request) {

        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Type mismatched",
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        ex.getPropertyName() + " was given with value " + ex.getValue(),
                        ex.getPropertyName() + " should be of type " + ex.getRequiredType()
                ))
        ));
    }


    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(@NonNull MissingServletRequestParameterException ex, 
                                                                          @NonNull HttpHeaders headers, 
                                                                          @NonNull HttpStatus status, 
                                                                          @NonNull WebRequest request) {
        
        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Servlet request parameter(s) is missing",
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        ex.getParameterName() + " is missing",
                        ex.getParameterName() + " should be of type " + ex.getParameterType()
                ))
        ));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingServletRequestPart(@NonNull MissingServletRequestPartException ex, 
                                                                     @NonNull HttpHeaders headers, 
                                                                     @NonNull HttpStatus status, 
                                                                     @NonNull WebRequest request) {
        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Servlet request part(s) is missing",
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        ex.getRequestPartName() + " is missing",
                        ex.getRootCause().getMessage()
                ))
        ));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMissingPathVariable(@NonNull MissingPathVariableException ex, 
                                                               @NonNull HttpHeaders headers, 
                                                               HttpStatus status, 
                                                               @NonNull WebRequest request) {
       
        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Path variable(s) is missing",
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        ex.getVariableName() + " is missing",
                        ex.getRootCause().getMessage()
                ))
        ));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpStatus status, WebRequest request) {

        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Types of method arguments are currently mismatch",
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        "'" + ex.getName() + "' parameter was given with " + ex.getValue(),
                        "'" + ex.getName() + "' should be type of " + ex.getRequiredType()
                ))
        ));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, HttpStatus status, WebRequest request) {
        final List<WebError.SubError> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(new WebError.SubError(
                    violation.getRootBeanClass().getName() + " " + violation.getPropertyPath(),
                    violation.getMessage()));
        }

        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Validation constraint has been violated",
                getExceptionClassname(ex),
                getRequestPath(request),
                errors
        ));
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        return ResponseEntity.badRequest().body(new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Http message not readable",
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        "Message not available",
                        ex.getLocalizedMessage().substring(0, ex.getLocalizedMessage().indexOf(":"))
                ))
        ));
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex,
                                                               HttpStatus status,
                                                               WebRequest request) {
        final WebError webError = new WebError(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                getExceptionClassname(ex),
                getRequestPath(request)
        );

        return ResponseEntity.badRequest().body(webError);
    }

    // 404 - Not Found exception handlers ====================================================================================

    @Override
    @NonNull
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatus status,
                                                                   @NonNull WebRequest request) {

        final WebError webError = new WebError(
                status.value(),
                status.getReasonPhrase(),
                "No handler found for " + ex.getHttpMethod() + ": " + ex.getRequestURL(),
                getExceptionClassname(ex),
                getRequestPath(request)
        );

        return new ResponseEntity<>(webError, NOT_FOUND);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     @NonNull HttpHeaders headers,
                                                                     @NonNull HttpStatus status,
                                                                     @NonNull WebRequest request) {
        final WebError webError = new WebError(
                status.value(),
                status.getReasonPhrase(),
                "Supported media types are " + Joiner.on(", ").join(ex.getSupportedMediaTypes()),
                getExceptionClassname(ex),
                getRequestPath(request),
                List.of(new WebError.SubError(
                        "Content-Type is not supported or not set correctly",
                        "Content-Type header given with value - " + ex.getContentType()
                ))
        );

        return new ResponseEntity<>(webError, UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                     WebRequest request) {
        final WebError webError = new WebError(
                NOT_FOUND.value(),
                NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                getExceptionClassname(ex),
                getRequestPath(request)
        );

        return new ResponseEntity<>(webError, NOT_FOUND);
    }

    @Override
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        final WebError response = new WebError(
                status.value(),
                status.getReasonPhrase(),
                body == null ? ex.getCause().toString() : body.toString(),
                getExceptionClassname(ex),
                getRequestPath(request)
        );

        return ResponseEntity.badRequest().body(response);
    }

}
