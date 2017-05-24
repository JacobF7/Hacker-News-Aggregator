package com.uom.assignment.model.request;

import com.uom.assignment.dao.Story;

import java.util.Objects;

/**
 * A model serving as placeholder for a {@link Story} to be updated.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
public class UpdateStoryModel {

    private final Story story;

    private final Long score;

    private final boolean deleted;

    public UpdateStoryModel(final Story story, final Long score, final Boolean deleted) {
        this.story = story;
        this.score = score;
        this.deleted = deleted;
    }

    public Story getStory() {
        return story;
    }

    public Long getScore() {
        return score;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UpdateStoryModel that = (UpdateStoryModel) o;

        return Objects.equals(getStory(), that.getStory()) &&
                Objects.equals(getScore(), that.getScore()) &&
                Objects.equals(isDeleted(), that.isDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStory(), getScore(), isDeleted());
    }
}
