package com.uom.assignment.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An exception handler that serves to handle multiple exceptions.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

    private static final String ERROR = "Error";
    private static final String ERRORS = "Errors";

    /**
     * An exception mapper to handle all {@link BusinessErrorException}s.
     *
     * @param ex the {@link BusinessErrorException}.
     * @param request the {@link WebRequest}.
     * @return a {@link ResponseEntity} containing the {@link BusinessErrorException#getMessage()}.
     */
    @ExceptionHandler(BusinessErrorException.class)
    public ResponseEntity<Object> handleBusinessErrorException(final BusinessErrorException ex, final WebRequest request) {
        logger.error(ex);
        return constructResponse(ex, request, ex.getStatusCode(), Collections.singletonMap(ERROR, ex.getMessage()));
    }

    /**
     * An exception mapper to handle all {@link MethodArgumentNotValidException}s which is thrown when validation on an argument annotated with {@code @Valid} fails.
     *
     * @param ex the {@link MethodArgumentNotValidException}.
     * @param headers the {@link HttpHeaders}.
     * @param status the {@link HttpStatus}.
     * @param request the {@link WebRequest}.
     * @return a {@link ResponseEntity} containing all {@link FieldError}s in the {@link MethodArgumentNotValidException#getBindingResult}.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        logger.error(ex);
        final List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        final Map<String, String> errorMessages = errors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return constructResponse(ex, request, HttpStatus.BAD_REQUEST, Collections.singletonMap(ERRORS, errorMessages));
    }

    /**
     * An exception mapper serving as a catch all for any other {@link RuntimeException}.
     *
     * @param ex the {@link RuntimeException}.
     * @param request the {@link WebRequest}.
     * @return a {@link ResponseEntity} containing the {@link RuntimeException#getMessage()}.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(final RuntimeException ex, final WebRequest request) {
        logger.error(ex.getMessage(), ex.getCause());
        return constructResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, Collections.singletonMap(ERROR, ex.getMessage()));
    }


    private ResponseEntity<Object> constructResponse(final Exception e,
                                                     final WebRequest request,
                                                     final HttpStatus status,
                                                     final Map<String, Object> errors) {
        return handleExceptionInternal(e, errors, constructHttpHeaders(), status, request);
    }

    private HttpHeaders constructHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}

