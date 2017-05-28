package com.uom.assignment.model.response;

import com.uom.assignment.dao.Story;

import java.util.Objects;

/**
 * A model containing the {@link Story#title}, {@link Story#score} and {@link Story#url} for a particular {@link Story}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
public class StoryModel {

    private final String title;
    private final String url;
    private final Long score;

    private StoryModel() {
        this.title = null;
        this.url = null;
        this.score = null;
    }

    private StoryModel(final String title, final String url, final Long score) {
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

    public static StoryModel of(final Story story) {

        if(Objects.isNull(story)) {
            return new StoryModel();
        }

        return new StoryModel(story.getTitle(), story.getUrl(), story.getScore());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final StoryModel that = (StoryModel) o;
        return Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getUrl(), that.getUrl()) &&
                Objects.equals(getScore(), that.getScore());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getUrl(), getScore());
    }
}
