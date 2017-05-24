package com.uom.assignment.dao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link Entity} for a User.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Session> sessions;

    @OneToMany(mappedBy = "user")
    private Set<UserTopic> userTopics = new HashSet<>();

    public User() {
        // Needed by Hibernate
    }

    public User(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(final Set<Session> sessions) {
        this.sessions = sessions;
    }

    public Set<UserTopic> getUserTopics() {
        return userTopics;
    }

    public void setUserTopics(final Set<UserTopic> userTopics) {
        this.userTopics = userTopics;
    }

    public Set<UserTopic> getEffectiveUserTopics() {
        return userTopics.stream().filter(UserTopic::isEffective).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User user = (User) o;

        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword());
    }
}
