package com.uom.assignment.service;

import com.uom.assignment.controller.BusinessError;
import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.Session;
import com.uom.assignment.dao.User;
import com.uom.assignment.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link Service} implementation for the {@link Session} entity.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    private final UserService userService;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SessionServiceImpl(final UserService userService, final SessionRepository sessionRepository, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(final String username, final String password) {

        final Optional<User> optionalUser = userService.findByUsername(username);

        if(!optionalUser.isPresent()) {
            throw new BusinessErrorException(BusinessError.INVALID_CREDENTIALS);
        }

        final User user = optionalUser.get();

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessErrorException(BusinessError.INVALID_CREDENTIALS);
        }

        final Optional<Session> activeSession = findActiveSessionByUserId(user.getId());

        // If an active session is present, refresh the token and return it
        if(activeSession.isPresent()) {
            final Session session = activeSession.get();
            refresh(session);
            return session.getToken();
        }

        // If an active session is NOT present, create a new session
        final Session session = new Session(user);
        sessionRepository.save(session);
        return session.getToken();
    }

    @Override
    public Optional<Session> findActiveSessionByUserId(final Long id) {

        final List<Session> sessions = sessionRepository.findByUserId(id);

        return sessions.stream()
                       .filter(SessionService::isSessionActive)
                       .findFirst();
    }

    @Override
    public Long authenticate(final String token) {

        // Check if the token exists
        final Session session = findByToken(token).orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_TOKEN));

        // Check whether the token is expired
        if (SessionService.isSessionExpired(session)) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        // If the Session is active, we refresh it
        refresh(session);

        return session.getUser().getId();
    }

    @Override
    public void logout(final String token) {
        sessionRepository.deleteByToken(token);
    }

    @Override
    public Optional<Session> findByToken(final String token) {
        return sessionRepository.findByToken(token);
    }

    @Override
    public void refresh(final Session session) {
        session.setLastActivity(System.currentTimeMillis());
        sessionRepository.save(session);
    }

    @Override
    public Set<Long> deleteExpiredSessions() {

        final Set<Long> expiredSessions = sessionRepository.findAll()
                                                           .parallelStream()
                                                           .filter(SessionService::isSessionExpired)
                                                           .map(Session::getId)
                                                           .collect(Collectors.toSet());

        expiredSessions.forEach(sessionRepository::delete);

        return expiredSessions;
    }
}
