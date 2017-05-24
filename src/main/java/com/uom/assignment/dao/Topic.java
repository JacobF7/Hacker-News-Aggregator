package com.uom.assignment.dao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The {@link Entity} for a Topic.
 *
 * Created by jacobfalzon on 06/05/2017.
 */
@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.MERGE)
    private Set<UserTopic> userTopics = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "top_story_id")
    private Story topStory;

    public Topic() {
        // Needed by Hibernate
    }

    public Topic(final String name, final Story topStory) {
        this.name = name;
        this.topStory = topStory;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<UserTopic> getUserTopics() {
        return userTopics;
    }

    public void setUserTopics(final Set<UserTopic> userTopics) {
        this.userTopics = userTopics;
    }

    public Story getTopStory() {
        return topStory;
    }

    public void setTopStory(final Story topStory) {
        this.topStory = topStory;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Topic topic = (Topic) o;

        return Objects.equals(getId(), topic.getId()) &&
                Objects.equals(getName(), topic.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
