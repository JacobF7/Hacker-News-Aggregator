package com.uom.assignment.dao;

import javax.persistence.*;
import java.util.Objects;

/**
 * The {@link Entity} for the relationship between a {@link User} and a {@link Topic}.
 * This {@link Entity also defines the timestamp until the relationship is effective.
 *
 * Created by jacobfalzon on 11/05/2017.
 */
@Entity
@Table(name = "user_topic")
public class UserTopic {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "effective_from")
    private Long effectiveFrom;

    @Column(name = "effective_to")
    private Long effectiveTo;

    public UserTopic(final User user, final Topic topic) {
        this.user = user;
        this.topic = topic;
        this.effectiveFrom = System.currentTimeMillis();
    }

    public UserTopic() {
        // Needed by Hibernate
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(final Topic topic) {
        this.topic = topic;
    }

    public Long getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(Long effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Long getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(final Long effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public boolean isEffective() {
        return effectiveTo == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UserTopic userTopic = (UserTopic) o;

        return Objects.equals(getId(), userTopic.getId()) &&
                Objects.equals(getUser(), userTopic.getUser()) &&
                Objects.equals(getTopic(), userTopic.getTopic()) &&
                Objects.equals(getEffectiveFrom(), userTopic.getEffectiveFrom()) &&
                Objects.equals(getEffectiveTo(), userTopic.getEffectiveTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getTopic(), getEffectiveFrom(), getEffectiveTo());
    }
}
