package com.uom.assignment.aspect;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker {@link Annotation} that is used to identify the Header containing the Authorization Token.
 *
 * Created by jacobfalzon on 08/05/2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AuthorizationHeader {
   /**
    * The Authorization Header Key.
    */
   String AUTHORIZATION_HEADER = "Authorization";
}
