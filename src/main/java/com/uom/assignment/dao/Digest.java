package com.uom.assignment.dao;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;

/**
 * The {@link Entity} for a Digest.
 * A digest is valid for a particular time-span, i.e. the duration between {@link Digest#effectiveFrom} and {@link Digest#effectiveTo}.
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

    @Column(name = "effective_from")
    private Long effectiveFrom;

    @Column(name = "effective_to")
    private Long effectiveTo;

    public Digest(final Topic topic, final Story story, final Long effectiveFrom, final Long effectiveTo) {
        this.topic = topic;
        this.story = story;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
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

    public Long getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(final Long effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public Long getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(final Long effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    @Override
    public boolean equals(Object o) {
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
                Objects.equals(getEffectiveFrom(), digest.getEffectiveFrom()) &&
                Objects.equals(getEffectiveTo(), digest.getEffectiveTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTopic(), getStory(), getEffectiveFrom(), getEffectiveTo());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("topic", topic)
                .add("story", story)
                .add("effectiveFrom", effectiveFrom)
                .add("effectiveTo", effectiveTo)
                .toString();
    }
}
