package com.uom.assignment.dao;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@link Entity} for a Session.
 * The Session contains the Token that is required by a {@link User} to perform any operation.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(unique = true)
    private String token;

    @Column(name = "last_activity")
    private Long lastActivity;

    public Session() {
        // Needed by Hibernate
    }

    public Session(final User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        lastActivity = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public Long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(final Long lastActivity) {
        this.lastActivity = lastActivity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Session session = (Session) o;

        return Objects.equals(getId(), session.getId()) &&
                Objects.equals(getToken(), session.getToken()) &&
                Objects.equals(getLastActivity(), session.getLastActivity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getLastActivity());
    }
}
