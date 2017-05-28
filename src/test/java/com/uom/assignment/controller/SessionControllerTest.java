package com.uom.assignment.controller;

import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.model.request.UserLoginModel;
import com.uom.assignment.model.response.TokenModel;
import com.uom.assignment.service.SessionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * A test suite for {@link SessionController}.
 *
 * Created by jacobfalzon on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionController sessionController;

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final UserLoginModel USER_LOGIN_MODEL = new UserLoginModel(USERNAME, PASSWORD);

    private static final String TOKEN = "token";

    @Test
    public void login_delegatesToSessionService_returnsTokenModel() {
        // Mocking that the login is successful and TOKEN is returned
        Mockito.when(sessionService.login(USER_LOGIN_MODEL.getUsername(), USER_LOGIN_MODEL.getPassword())).thenReturn(TOKEN);

        final ResponseEntity<TokenModel> response = sessionController.login(USER_LOGIN_MODEL);

        // Verifying the login was attempted
        Mockito.verify(sessionService).login(USER_LOGIN_MODEL.getUsername(), USER_LOGIN_MODEL.getPassword());

        // Verifying the TOKEN was returned
        Assert.assertEquals(TOKEN, response.getBody().getToken());
    }

    @Test
    public void logout_delegatesToSessionService() {

        Mockito.when(request.getHeader(AuthorizationHeader.AUTHORIZATION_HEADER)).thenReturn(TOKEN);

        sessionController.logout(request);

        Mockito.verify(sessionService).logout(TOKEN);
    }
}
