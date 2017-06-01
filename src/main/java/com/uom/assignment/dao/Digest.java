package com.uom.assignment.dao;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@link Entity} for a Digest.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Entity
@Table(name = "digest")
public class Digest {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "top_story_id")
    private Story story;

    @Column(name = "creation_date")
    private Long creationDate;

    @ManyToMany(cascade = CascadeType.MERGE) // TODO CONFIRM CASCADE TYPE
    @JoinTable(name = "user_digest",
               joinColumns = @JoinColumn(name = "digest_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    private Boolean overall;

    public Digest() {
        // needed by hibernate
    }

    public Digest(final Topic topic, final Story story, final Long creationDate, final Set<User> users, final Boolean overall) {
        this.topic = topic;
        this.story = story;
        this.creationDate = creationDate;
        this.users = users;
        this.overall = overall;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(final Topic topic) {
        this.topic = topic;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(final Story story) {
        this.story = story;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Long creationDate) {
        this.creationDate = creationDate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(final Set<User> users) {
        this.users = users;
    }

    public Boolean getOverall() {
        return overall;
    }

    public void setOverall(final Boolean overall) {
        this.overall = overall;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Digest digest = (Digest) o;

        return Objects.equals(getId(), digest.getId()) &&
                Objects.equals(getTopic(), digest.getTopic()) &&
                Objects.equals(getStory(), digest.getStory()) &&
                Objects.equals(getCreationDate(), digest.getCreationDate()) &&
                Objects.equals(getOverall(), digest.getOverall());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTopic(), getStory(), getCreationDate(), getOverall());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("topic", topic)
                .add("story", story)
                .add("creationDate", creationDate)
                .add("overall", overall)
                .add("users", users.stream().map(User::getId).collect(Collectors.toSet()))
                .toString();
    }
}
