package com.uom.assignment.model.request;

import com.uom.assignment.dao.User;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The model for the {@link User} entity.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
public class UserModel {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Set<String> topics;

    public UserModel() {
    }

    public UserModel(final String username, final String password, final Set<String> topics) {
        this.username = username;
        this.password = password;
        this.topics = topics;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getTopics() {
        return topics;
    }
}
