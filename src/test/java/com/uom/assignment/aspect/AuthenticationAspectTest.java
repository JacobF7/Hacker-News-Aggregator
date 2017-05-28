package com.uom.assignment.aspect;

import com.uom.assignment.service.SessionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;
import java.util.UUID;

/**
 * A test suite for {@link AuthenticationAspect}.
 *
 * Created by jacobfalzon on 09/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationAspectTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthenticationAspect authenticationAspect;

    private static final String SESSION_TOKEN = UUID.randomUUID().toString();
    private static final Long USER_ID = new Random().nextLong();

    @Test
    public void authenticate_delegatesToSessionService() {

        // Mocking that the mockRequest contains SESSION_TOKEN
        Mockito.when(mockRequest.getHeader(AuthorizationHeader.AUTHORIZATION_HEADER)).thenReturn(SESSION_TOKEN);

        // Mocking that USER_ID is returned by after authentication
        Mockito.when(sessionService.authenticate(SESSION_TOKEN)).thenReturn(USER_ID);

        authenticationAspect.authenticate(mockRequest);

        // Verifying that an attempt to authenticate the Session is made
        Mockito.verify(sessionService).authenticate(SESSION_TOKEN);

        // Verifying that USER_ID was set in the request
        Mockito.verify(mockRequest).setAttribute(AuthorizationHeader.USER_ID, USER_ID);
    }

    @Test
    public void requestPointcut_requestContainsToken() {

        // Mocking that the mockRequest contains SESSION_TOKEN
        Mockito.when(mockRequest.getHeader(AuthorizationHeader.AUTHORIZATION_HEADER)).thenReturn(SESSION_TOKEN);

        authenticationAspect.requestPointcut(mockRequest);

        // Verifying that mockRequest contains SESSION_TOKEN
        Assert.assertEquals(SESSION_TOKEN, mockRequest.getHeader(AuthorizationHeader.AUTHORIZATION_HEADER));
    }

}
