package com.uom.assignment.controller;

import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.model.request.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @RequestMapping(method = RequestMethod.PUT)
    public void testModelValidation(final @RequestBody @Valid UserModel userModel) {}

    @RequestMapping(method = RequestMethod.PATCH)
    public String testRuntimeException() {
        throw new RuntimeException("Some error occurred");
    }

    @RequestMapping(method = RequestMethod.GET)
    public void testAuthenticationAspect(@AuthorizationHeader @RequestHeader(AuthorizationHeader.AUTHORIZATION_HEADER) final String authorization) {

    }
}
