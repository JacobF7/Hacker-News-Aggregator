package com.uom.assignment.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;

/**
 * A test suite for {@link ExceptionMapper}.
 *
 * Created by jacobfalzon on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExceptionMapperTest {

    @Mock
    private WebRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult result;

    @Mock
    private HttpHeaders headers;

    private final ExceptionMapper exceptionMapper = new ExceptionMapper();

    @Test
    public void handleBusinessErrorException_responseContainsStatusCode_responseContainsErrorMessage() {

        final ResponseEntity<Object> response = exceptionMapper.handleBusinessErrorException(new BusinessErrorException(BusinessError.INVALID_CREDENTIALS), request);

        // Verifying status code
        Assert.assertEquals(BusinessError.INVALID_CREDENTIALS.getStatusCode(), response.getStatusCode());

        // Verifying error message
        Assert.assertTrue(response.getBody().toString().contains(BusinessError.INVALID_CREDENTIALS.getErrorMessage()));
    }

    @Test
    public void handleBusinessErrorException_customError_responseContainsStatusCode_responseContainsErrorMessage() {

        final String error = "Custom Error";
        final HttpStatus status = HttpStatus.CONFLICT;
        final ResponseEntity<Object> response = exceptionMapper.handleBusinessErrorException(new BusinessErrorException(error, status), request);

        // Verifying status code
        Assert.assertEquals(status, response.getStatusCode());

        // Verifying error message
        Assert.assertTrue(response.getBody().toString().contains(error));
    }

    @Test
    public void handleMethodArgumentNotValidException_responseContainsStatusCode_responseContainsErrorMessage() {

        final String object = "Object";
        final String field = "Field";
        final String error = "Validation Error";
        final FieldError fieldError = new FieldError(object, field, error);

        // Mocking a Field Error in the result of the methodArgumentNotValidException
        Mockito.doReturn(Collections.singletonList(fieldError)).when(result).getFieldErrors();
        Mockito.when(methodArgumentNotValidException.getBindingResult()).thenReturn(result);

        final ResponseEntity<Object> response = exceptionMapper.handleMethodArgumentNotValid(methodArgumentNotValidException, headers, HttpStatus.BAD_REQUEST, request);

        // Verifying status code
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verifying error message
        Assert.assertTrue(response.getBody().toString().contains(error));
    }

    @Test
    public void handleBindException_responseContainsStatusCode_responseContainsErrorMessage() {

        final String object = "Object";
        final String field = "Field";
        final String error = "Validation Error";
        final FieldError fieldError = new FieldError(object, field, error);
        final BindException bindException = new BindException(result);

        // Mocking a Field Error in the result of the bindException
        Mockito.doReturn(Collections.singletonList(fieldError)).when(result).getFieldErrors();

        final ResponseEntity<Object> response = exceptionMapper.handleBindException(bindException, headers, HttpStatus.BAD_REQUEST, request);

        // Verifying status code
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verifying error message
        Assert.assertTrue(response.getBody().toString().contains(error));
    }

    @Test
    public void handleRuntimeException_responseContainsStatusCode_responseContainsErrorMessage() {

        final String error = "Unexpected Error";
        final ResponseEntity<Object> response = exceptionMapper.handleRuntimeException(new RuntimeException(error), request);

        // Verifying status code
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Verifying error message
        Assert.assertTrue(response.getBody().toString().contains(error));
    }

}
