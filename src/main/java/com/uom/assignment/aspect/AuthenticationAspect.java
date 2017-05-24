package com.uom.assignment.aspect;

import com.uom.assignment.dao.Session;
import com.uom.assignment.service.SessionService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * An {@link Aspect} serving to authenticate any protected API.
 *
 * Created by jacobfalzon on 08/05/2017.
 */
@Aspect
@Component
public class AuthenticationAspect {

    private final SessionService sessionService;

    @Autowired
    public AuthenticationAspect(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * This {@link Pointcut} is used to identify the Authorization Token that is annotated by the {@link AuthorizationHeader} and {@link RequestHeader} annotations.
     *
     * @param authorization the authorization token annotated by the {@link AuthorizationHeader} and {@link RequestHeader}.
     */
    @Pointcut("execution(* *(@AuthorizationHeader (String),..)) && execution(* *(@org.springframework.web.bind.annotation.RequestHeader (String),..)) && args(authorization,..)")
    public void authorizationHeaderPointcut(final String authorization){}

    /**
     * The {@link Before} advice serving to authenticate any protected API.
     * If the {@code authorization} token is valid, the associated {@link Session} is refreshed.
     *
     * @param authorization the authorization token annotated by {@link AuthorizationHeader} and {@link RequestHeader}.
     */
    @Before("authorizationHeaderPointcut(authorization)")
    public void authenticate(final String authorization) {
        sessionService.authenticate(authorization);
    }
}
