package com.uom.assignment.model.request;

import com.uom.assignment.dao.User;

import javax.validation.constraints.NotNull;

/**
 * The model for the {@link User#username}, {@link User#password} attributes required during Login.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
public class UserLoginModel {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public UserLoginModel() {
    }

    public UserLoginModel(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
