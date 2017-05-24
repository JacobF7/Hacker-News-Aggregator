package com.uom.assignment.aspect;

import com.uom.assignment.service.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

/**
 * A test suite for {@link AuthenticationAspect}.
 *
 * Created by jacobfalzon on 09/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationAspectTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthenticationAspect authenticationAspect;

    private static final String SESSION_TOKEN = UUID.randomUUID().toString();

    @Test
    public void authenticate_delegatesToSessionService() {
        authenticationAspect.authenticate(SESSION_TOKEN);

        // Verifying that an attempt to authenticate the Session is made
        Mockito.verify(sessionService).authenticate(SESSION_TOKEN);
    }

}
