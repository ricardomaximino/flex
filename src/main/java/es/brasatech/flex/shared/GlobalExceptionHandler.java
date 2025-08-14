package es.brasatech.flex.shared;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse validationError(HttpServletRequest request, ValidationException exception){
        log.warn("Validation exception error: {}", exception.getMessage());
        return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getMessage()).instance(URI.create(request.getRequestURI())).build();
    }
}
