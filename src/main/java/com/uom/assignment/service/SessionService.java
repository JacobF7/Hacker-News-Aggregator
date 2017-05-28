package com.uom.assignment.service;

import com.uom.assignment.aspect.AuthenticationAspect;
import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.Session;
import com.uom.assignment.dao.User;

import javax.transaction.Transactional;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.currentTimeMillis;
import static java.time.Duration.between;
import static java.time.Instant.ofEpochMilli;

/**
 * A service containing all {@link Session} related operations.
 *
 * Created by jacobfalzon on 29/04/2017.
 */
public interface SessionService {

    /**
     * Authenticates the given {@code username} and {@code password} for a particular {@link User}.
     * On successful login, this method returns a {@link Session#token}.
     *
     * Note that if an active {@link Session#token} already exists, it is returned.
     *
     * @param username the {@link User#username} of the desired {@link User}
     * @param password the {@link User#password} of the desired {@link User}.
     * @throws BusinessErrorException if a {@link User} with the given {@code username} and {@code password} cannot be resolved.
     * @return a {@link Session#token} for the authenticated {@link User}.
     */
    String login(String username, String password);

    /**
     * Find an active {@link Session} with the given {@link User#id}.
     * Note that there should only be one active {@link Session} for a particular {@link User}.
     *
     * @param id the {@link User#id} of the desired {@link User}.
     * @return an {@link Optional} containing the {@link Session} if the {@link User} with the given {@link User#id} has an active {@link Session} associated to it, otherwise {@link Optional#empty()}.
     */
    Optional<Session> findActiveSessionByUserId(Long id);

    /**
     * Attempts to authenticate a {@link Session#token}.
     *
     * @param token the {@link Session#token} to authenticate.
     * @return the {@link User#id} resolved from the {@link Session}, if the {@link Session#token} is successfully authenticated.
     * @throws BusinessErrorException when the specified {@link Session#token} is already expired or does not exist.
     */
    Long authenticate(String token);

    /**
     * Expire any active {@link Session} with the given {@link Session#token}.
     * Note that given the {@link Session#token} is always valid, i.e. it exists and belongs to an active {@link Session}.
     * This is because the given {@link Session#token} is first authenticated via the {@link AuthenticationAspect} before this method is invoked.
     *
     * @param token the {@link Session#token} to expire.
     */
    void logout(String token);

    /**
     * Find a {@link Session} with the given {@link Session#token}.
     * Note that the {@link Session#token} is unique across all sessions.
     *
     * @param token the {@link Session#token} of the desired {@link Session}.
     * @return an {@link Optional} containing the {@link Session} if a {@link Session} exists with the given {@code token}, otherwise {@link Optional#empty()}.
     */
    Optional<Session> findByToken(String token); // TODO CACHE THIS

    /**
     * Refreshes the given {@link Session} by setting {@link Session#lastActivity} to {@link System#currentTimeMillis()}.
     *
     * @param session the {@link Session} to be refreshed.
     */
    void refresh(Session session);

    /**
     * Deletes any expired {@link Session}, i.e. any {@link Session}
     *
     * @return a {@link Set} of {@link Session#id} for every expired {@link Session} that is deleted.
     */
    Set<Long> deleteExpiredSessions();

    /**
     * Determines whether the given {@link Session} is expired, i.e. the duration between now and {@link Session#lastActivity} is longer than the default session expiration time.
     *
     * @param session the {@link Session} to check for expiration.
     * @return true if the given {@link Session} is expired, false otherwise.
     */
    static boolean isSessionExpired(Session session) {
        return between(ofEpochMilli(session.getLastActivity()),
                       ofEpochMilli(currentTimeMillis())).toMillis() > Duration.ofMinutes(30L).toMillis();
    }

    /**
     * Determines whether the given {@link Session} is active, i.e. the duration between now and {@link Session#lastActivity} is NOT longer than the default session expiration time.
     *
     * @param session the {@link Session} to check for expiration.
     * @return true if the given {@link Session} is expired, false otherwise.
     */
    static boolean isSessionActive(Session session) {
        return !isSessionExpired(session);
    }

    /**
     * @return the default {@link Session} expiry time in milliseconds.
     */
    static Long getSessionExpiryTime() {
        return Duration.ofMinutes(30L).toMillis();
    }
}
