package com.uom.assignment.hacker.news.api.response;

import com.google.common.base.MoreObjects;
import com.uom.assignment.hacker.news.api.request.ItemRequest;
import com.uom.assignment.hacker.news.api.request.NewStoriesRequest;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * The {@link HackerNewsResponse} for the {@link ItemRequest}.
 *
 * Created by jacobfalzon on 17/05/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponse implements HackerNewsResponse {

    private Long id;
    private String type;
    private String by;
    private Long time;
    private Long score;
    private String title;
    private String url;
    private Boolean deleted = false; // default value

    public ItemResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(final String by) {
        this.by = by;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(final Long time) {
        this.time = time;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(final Long score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("by", by)
                .add("time", time)
                .add("score", score)
                .add("title", title)
                .add("url", url)
                .toString();
    }
}
