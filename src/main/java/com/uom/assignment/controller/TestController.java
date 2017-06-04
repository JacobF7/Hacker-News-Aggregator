package com.uom.assignment.controller;

import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.model.request.DateRangeModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;

/**
 * A Controller used for testing purposes.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
@RestController
@RequestMapping(TestController.RESOURCE)
public class TestController {

    static final String RESOURCE = "/test";

    @RequestMapping(method = RequestMethod.POST)
    public String testBusinessErrorException() {
        throw new BusinessErrorException("Unauthorized", HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/model", method = RequestMethod.GET)
    public void testModelValidation(final @Valid DateRangeModel model) {
    }

    @RequestMapping(method = RequestMethod.PATCH)
    public String testRuntimeException() {
        throw new RuntimeException("Some error occurred");
    }

    @RequestMapping(method = RequestMethod.GET)
    public void testAuthenticationAspect(@AuthorizationHeader final HttpServletRequest request) {
        System.out.println(request.getAttribute(AuthorizationHeader.USER_ID));
    }
}

