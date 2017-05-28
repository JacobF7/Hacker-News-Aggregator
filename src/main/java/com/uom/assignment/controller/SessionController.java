package com.uom.assignment.controller;

import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.dao.Session;
import com.uom.assignment.model.request.UserLoginModel;
import com.uom.assignment.model.response.TokenModel;
import com.uom.assignment.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Controller for {@link Session} handling.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@RestController
@RequestMapping(SessionController.RESOURCE)
public class SessionController {

    static final String RESOURCE = "/sessions";

    private SessionService sessionService;

    @Autowired
    public SessionController(final SessionService sessionsService) {
        this.sessionService = sessionsService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TokenModel> login(@RequestBody @Valid final UserLoginModel userLoginModel) {
        final String token = sessionService.login(userLoginModel.getUsername(), userLoginModel.getPassword());
        return ResponseEntity.ok(new TokenModel(token));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void logout(@AuthorizationHeader final HttpServletRequest request) {
        final String token = request.getHeader(AuthorizationHeader.AUTHORIZATION_HEADER);
        sessionService.logout(token);
    }

}
