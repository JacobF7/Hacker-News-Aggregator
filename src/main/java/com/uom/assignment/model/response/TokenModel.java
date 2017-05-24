package com.uom.assignment.model.response;

import com.uom.assignment.dao.Session;

/**
 * The model containing the {@link Session#token} that is returned as a response on a successful login.
 *
 * Created by jacobfalzon on 14/05/2017.
 */
public class TokenModel {

    private final String token;

    public TokenModel(final String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
