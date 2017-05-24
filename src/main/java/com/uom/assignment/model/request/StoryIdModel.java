package com.uom.assignment.model.request;

import com.uom.assignment.dao.Story;

import java.util.Objects;

/**
 * A model serving as a placeholder for a {@link Story#hackerNewsId}.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
public class StoryIdModel {

    private String id;

    public StoryIdModel(final String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final StoryIdModel that = (StoryIdModel) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
