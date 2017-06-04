package com.uom.assignment.service;

import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.Session;
import com.uom.assignment.dao.User;
import com.uom.assignment.repository.SessionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * A test suite for {@link SessionService}.
 *
 * Created by jacobfalzon on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionServiceImplTest {

    @Mock
    private Session mockSession;

    @Mock
    private User mockUser;

    @Mock
    private UserService userService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SessionServiceImpl sessionService;

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String SESSION_TOKEN = UUID.randomUUID().toString();
    private static final Long USER_ID = RANDOM.nextLong();
    private static final Long SESSION_ID = RANDOM.nextLong();

    @Before
    public void setup() {

        // Mocking mockUser to contain SESSION_ID
        Mockito.when(mockSession.getId()).thenReturn(SESSION_ID);

        // Mocking mockSession to contain SESSION_TOKEN
        Mockito.when(mockSession.getToken()).thenReturn(SESSION_TOKEN);
        Mockito.when(sessionRepository.findByToken(SESSION_TOKEN)).thenReturn(Optional.of(mockSession));

        // Mocking mockUser to contain USER_ID
        Mockito.when(mockUser.getId()).thenReturn(USER_ID);

        // Mocking mockSession to contain mockUser
        Mockito.when(mockSession.getUser()).thenReturn(mockUser);
        Mockito.doReturn(Collections.singletonList(mockSession)).when(sessionRepository).findByUserId(USER_ID);
    }

    @Test
    public void refresh_sessionTokenRefreshed() {
        sessionService.refresh(mockSession);
        Mockito.verify(mockSession).setLastActivity(Matchers.anyLong());
    }

    @Test
    public void findByToken_returnsSession() {
        final Optional<Session> session = sessionService.findByToken(SESSION_TOKEN);
        Assert.assertEquals(Optional.of(mockSession), session);
    }

    @Test
    public void findActiveSessionByUserId_sessionNotExpired_returnsSession() {

        // Mock lastActivity as now, i.e. mockSession is not expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(System.currentTimeMillis());

        final Optional<Session> session = sessionService.findActiveSessionByUserId(USER_ID);
        Assert.assertEquals(Optional.of(mockSession), session);
    }

    @Test
    public void findActiveSessionByUserId_sessionExpired_returnsEmptyOptional() {

        // Mock lastActivity as 30 minutes ago, i.e. mockSession is expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(getExpiredLastActivity());

        final Optional<Session> session = sessionService.findActiveSessionByUserId(USER_ID);
        Assert.assertEquals(Optional.empty(), session);
    }

    @Test(expected = BusinessErrorException.class)
    public void login_usernameDoesNotExist_throwsBusinessErrorException() {

        // Mocking no User with the given username
        final String username = "username";
        final String password = "password";
        Mockito.when(userService.findByUsername(username)).thenReturn(Optional.empty());

        sessionService.login(username, password);
    }

    @Test(expected = BusinessErrorException.class)
    public void login_passwordDoesNotMatch_throwsBusinessErrorException() {

        // Mocking mockUser to exist with the given username
        final String username = "username";
        final String password = "wrongPassword";
        Mockito.when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Mocking mockUser credentials, i.e. the username matches but the password does NOT
        Mockito.when(mockUser.getUsername()).thenReturn(username);
        Mockito.when(mockUser.getPassword()).thenReturn("goodPassword");

        // Mocking that the passwords do not match
        Mockito.when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);

        sessionService.login(username, password);
    }

    @Test
    public void login_activeSessionExists_returnsActiveSessionToken() {

        // Mocking mockUser to exist with the given username and password
        final String username = "username";
        final String password = "password";
        Mockito.when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Mocking mockUser credentials, i.e. the username and password are correct
        Mockito.when(mockUser.getUsername()).thenReturn(username);
        Mockito.when(mockUser.getPassword()).thenReturn(password);

        // Mocking that the passwords match
        Mockito.when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

        // A session, i.e. mockSession, exists for this mockUser (see Setup)
        // Mock lastActivity as now for thi, i.e. mockSession is not expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(System.currentTimeMillis());

        final String token = sessionService.login(username, password);

        // Assert that the token is equal to the token for mockSession
        Assert.assertEquals(mockSession.getToken(), token);

        // Verifying that a new Session was not created
        Mockito.verify(sessionRepository, Mockito.never()).save(Matchers.any(Session.class));
    }

    @Test
    public void login_sessionExpired_returnsNewSessionToken() {

        // Mocking a user with the given username
        final String username = "username";
        final String password = "password";
        Mockito.when(userService.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Mocking the User obtained, i.e. the username and password are correct
        Mockito.when(mockUser.getUsername()).thenReturn(username);
        Mockito.when(mockUser.getPassword()).thenReturn(password);

        // Mocking that the passwords match
        Mockito.when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

        // A session, i.e. mockSession, exists for this mockUser (see Setup)
        // Mock lastActivity as 30 minutes ago, i.e. mockSession is expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(getExpiredLastActivity());

        final String token = sessionService.login(username, password);

        // Assert a token is returned for the new Session
        Assert.assertNotNull(token);

        // Verifying that a new Session was actually created
        Mockito.verify(sessionRepository).save(Matchers.any(Session.class));
    }

    @Test(expected = BusinessErrorException.class)
    public void authenticate_invalidToken_throwsBusinessErrorException() {

        final String INVALID_TOKEN = "invalidToken";
        Mockito.when(sessionRepository.findByToken(INVALID_TOKEN)).thenReturn(Optional.empty());

        sessionService.authenticate(INVALID_TOKEN);
    }

    @Test(expected = BusinessErrorException.class)
    public void authenticate_expiredToken_throwsBusinessErrorException() {

        // Mock lastActivity as 30 minutes ago, i.e. mockSession is expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(getExpiredLastActivity());

        sessionService.authenticate(mockSession.getToken());
    }

    @Test
    public void authenticate_validToken_sessionRefreshed_returnsUserId() {

        // Mock lastActivity as now, i.e. mockSession is not expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(System.currentTimeMillis());

        final Long userId = sessionService.authenticate(mockSession.getToken());

        // Verifying Session is refreshed
        Mockito.verify(mockSession).setLastActivity(Matchers.anyLong());

        // Verifying that USER_ID is returned
        Assert.assertEquals(USER_ID, userId);
    }

    @Test
    public void logout_sessionDeleted() {

        sessionService.logout(mockSession.getToken());

        // Verifying Token was deleted
        Mockito.verify(sessionRepository).deleteByToken(mockSession.getToken());
    }

    @Test
    public void deleteExpiredSessions_expiredSessionExists_expiredSessionDeleted() {

        // Mocking all sessions stored in database, i.e. mockSession
        Mockito.when(sessionRepository.findAll()).thenReturn(Collections.singletonList(mockSession));

        // Mock lastActivity as 30 minutes ago, i.e. mockSession is expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(getExpiredLastActivity());

        sessionService.deleteExpiredSessions();

        // Verifying that mockSession was deleted
        Mockito.verify(sessionRepository).delete(mockSession.getId());
    }

    @Test
    public void deleteExpiredSessions_expiredSessionDoesNotExist_doesNothing() {

        // Mocking all sessions stored in database, i.e. mockSession
        Mockito.when(sessionRepository.findAll()).thenReturn(Collections.singletonList(mockSession));

        // Mock lastActivity as now, i.e. mockSession is not expired.
        Mockito.when(mockSession.getLastActivity()).thenReturn(System.currentTimeMillis());

        sessionService.deleteExpiredSessions();

        // Verifying that mockSession was NOT deleted
        Mockito.verify(sessionRepository, Mockito.never()).delete(mockSession.getId());
    }

    private long getExpiredLastActivity() {
        // Return the lastActivity. This timestamp is expired by 1 minute.
        return System.currentTimeMillis() - SessionService.getSessionExpiryTime() - Duration.ofMinutes(1).toMillis();
    }

}
