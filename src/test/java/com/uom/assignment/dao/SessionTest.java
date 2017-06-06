package com.uom.assignment.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.UUID;

/**
 * A test suite for {@link Session}.
 *
 * Created by jacobfalzon on 06/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionTest {

    @Mock
    private User mockUser;

    @Mock
    private User newMockUser;

    private Session session;

    private static final String TOKEN = UUID.randomUUID().toString();
    private static final Long LAST_ACTIVITY = System.currentTimeMillis();

    private static final String NEW_TOKEN = UUID.randomUUID().toString();
    private static final Long NEW_LAST_ACTIVITY = System.currentTimeMillis();

    @Before
    public void setup() {
        session = new Session(mockUser);
        session.setToken(TOKEN);
        session.setLastActivity(LAST_ACTIVITY);
    }

    @Test
    public void createSession_containsAttributes() {

        // Verifying that the session contains mockUser, TOKEN, LAST_ACTIVITY
        Assert.assertEquals(mockUser, session.getUser());
        Assert.assertEquals(TOKEN, session.getToken());
        Assert.assertEquals(LAST_ACTIVITY, session.getLastActivity());
    }

    @Test
    public void updateSession_containsNewAttributes() {

        session.setUser(newMockUser);
        session.setToken(NEW_TOKEN);
        session.setLastActivity(NEW_LAST_ACTIVITY);

        // Verifying that the session was updated to contain mockNewUser, NEW_TOKEN and NEW_LAST_ACTIVITY
        Assert.assertEquals(newMockUser, session.getUser());
        Assert.assertEquals(NEW_TOKEN, session.getToken());
        Assert.assertEquals(NEW_LAST_ACTIVITY, session.getLastActivity());
    }

    @Test
    public void equals_sameObject_equal() {
        Assert.assertEquals(session, session);
    }

    @Test
    public void equals_differentObjects_sameAttributes_equal() {

        final Session otherSession = new Session(session.getUser());
        otherSession.setToken(session.getToken());
        otherSession.setLastActivity(session.getLastActivity());

        Assert.assertEquals(session, otherSession);
    }

    @Test
    public void equals_differentObjects_lastActivityIsDifferent_notEqual() {

        final Session otherSession = new Session(session.getUser());
        otherSession.setToken(session.getToken());
        otherSession.setLastActivity(Instant.now().plusSeconds(10).toEpochMilli());

        Assert.assertNotEquals(session, otherSession);
    }

    @Test
    public void equals_differentObjectType_notEqual() {
        Assert.assertNotEquals(session, new Object());
    }
}
