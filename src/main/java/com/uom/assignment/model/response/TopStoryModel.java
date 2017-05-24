package com.uom.assignment.model.response;

import com.uom.assignment.dao.Story;

import java.util.Objects;

/**
 * A model containing the {@link Story#title}, {@link Story#score} and {@link Story#url} for a particular {@link Story}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
public class TopStoryModel {

    private final String title;
    private final String url;
    private final Long score;

    private TopStoryModel() {
        this.title = null;
        this.url = null;
        this.score = null;
    }

    private TopStoryModel(final String title, final String url, final Long score) {
        this.title = title;
        this.url =url;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Long getScore() {
        return score;
    }

    public static TopStoryModel of(final Story story) {

        if(Objects.isNull(story)) {
            return new TopStoryModel();
        }

        return new TopStoryModel(story.getTitle(), story.getUrl(), story.getScore());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TopStoryModel that = (TopStoryModel) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getUrl(), that.getUrl()) &&
                Objects.equals(getScore(), that.getScore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getUrl(), getScore());
    }
}
