package com.uom.assignment.dao;


import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * The {@link Entity} for a Story retrieved from the Hacker News API.
 *
 * Created by jacobfalzon on 20/05/2017.
 */
@Entity
@Table(name = "story")
public class Story {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "hacker_news_id")
    private Long hackerNewsId;

    private String title;

    private String author;

    private String url;

    private Long score;

    private boolean deleted = false; // default is false

    @Column(name = "last_updated")
    private Long lastUpdated;

    public Story() {
        // Needed by Hibernate
    }

    public Story(final Long hackerNewsId, final String title, final String author, final String url, final Long score) {
        this.hackerNewsId = hackerNewsId;
        this.title = title;
        this.author = author;
        this.url = url;
        this.score = score;
        lastUpdated = System.currentTimeMillis();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getHackerNewsId() {
        return hackerNewsId;
    }

    public void setHackerNewsId(final Long hackerNewsId) {
        this.hackerNewsId = hackerNewsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(final Long score) {
        this.score = score;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(final boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isActive() {
        return !isDeleted();
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Story story = (Story) o;

        return Objects.equals(getId(), story.getId()) &&
               Objects.equals(getHackerNewsId(), story.getHackerNewsId()) &&
               Objects.equals(getTitle(), story.getTitle()) &&
               Objects.equals(getAuthor(), story.getAuthor()) &&
               Objects.equals(getUrl(), story.getUrl()) &&
               Objects.equals(getScore(), story.getScore()) &&
               Objects.equals(isDeleted(), story.isDeleted()) &&
               Objects.equals(getLastUpdated(), story.getLastUpdated()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHackerNewsId(), getTitle(), getAuthor(), getUrl(), getScore(), isDeleted(), getLastUpdated());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("hackerNewsId", hackerNewsId)
                .add("title", title)
                .add("author", author)
                .add("url", url)
                .add("score", score)
                .add("deleted", deleted)
                .add("lastUpdated", lastUpdated)
                .toString();
    }
}
