package com.uom.assignment.scheduler;

import com.uom.assignment.service.SessionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.SecureRandom;
import java.util.Collections;

/**
 * A test suite for {@link DeleteExpiredSessionsScheduledTask}.
 *
 * Created by jacobfalzon on 10/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteExpiredSessionsScheduledTaskTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private DeleteExpiredSessionsScheduledTask deleteExpiredSessionsScheduledTask;

    private static final Long DELETED_SESSION_ID = new SecureRandom().nextLong();

    @Test
    public void deleteExpiredSessions_delegatesToSessionService() {

        // Mocking a deleted Session
        Mockito.when(sessionService.deleteExpiredSessions()).thenReturn(Collections.singleton(DELETED_SESSION_ID));

        deleteExpiredSessionsScheduledTask.deleteExpiredSessions();

        Mockito.verify(sessionService).deleteExpiredSessions();
    }

}
