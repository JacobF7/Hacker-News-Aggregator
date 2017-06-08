package com.uom.assignment.scheduler;

import com.uom.assignment.service.DigestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.SecureRandom;
import java.util.Collections;

/**
 * A test suite for {@link DeleteExpiredDigestsScheduledTask}.
 *
 * Created by jacobfalzon on 02/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteExpiredDigestsScheduledTaskTest {

    @Mock
    private DigestService digestService;

    @InjectMocks
    private DeleteExpiredDigestsScheduledTask deleteExpiredDigestsScheduledTask;

    private static final Long EXPIRED_DIGEST_ID = new SecureRandom().nextLong();

    @Test
    public void deleteExpiredSessions_delegatesToSessionService() {

        // Mocking an expired Digest
        Mockito.when(digestService.deleteExpiredDigests()).thenReturn(Collections.singleton(EXPIRED_DIGEST_ID));

        deleteExpiredDigestsScheduledTask.deleteExpiredDigests();

        Mockito.verify(digestService).deleteExpiredDigests();
    }

}
