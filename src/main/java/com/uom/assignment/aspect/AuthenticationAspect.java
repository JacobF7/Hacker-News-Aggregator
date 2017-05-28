package com.uom.assignment.aspect;

import com.uom.assignment.dao.Session;
import com.uom.assignment.service.SessionService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
     * This {@link Pointcut} is used to identify the {@link HttpServletRequest} containing the Authorization Token Header that is annotated by the {@link AuthorizationHeader} annotation.
     *
     * @param request the {@link HttpServletRequest} containing the Authorization Token Header annotated by the {@link AuthorizationHeader} annotation.
     */
    @Pointcut("execution(* *(@AuthorizationHeader (javax.servlet.http.HttpServletRequest),..)) && args(request,..)")
    public void requestPointcut(final HttpServletRequest request){}

    /**
     * The {@link Before} advice serving to authenticate any protected API.
     * If the Authorization Token Header is valid, the associated {@link Session} is refreshed and the .
     *
     * @param request the {@link HttpServletRequest} containing the Authorization Token Header annotated by the {@link AuthorizationHeader} annotation.
     */
    @Before("requestPointcut(request)")
    public void authenticate(final HttpServletRequest request) {
        final String authorizationToken = request.getHeader(AuthorizationHeader.AUTHORIZATION_HEADER);
        final Long userId = sessionService.authenticate(authorizationToken);
        request.setAttribute(AuthorizationHeader.USER_ID, userId);
    }
}
