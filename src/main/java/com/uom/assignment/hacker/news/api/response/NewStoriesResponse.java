package com.uom.assignment.hacker.news.api.response;

import com.google.common.base.MoreObjects;
import com.uom.assignment.hacker.news.api.request.NewStoriesRequest;

/**
 * The {@link HackerNewsResponse} for the {@link NewStoriesRequest}.
 *
 * Created by jacobfalzon on 16/05/2017.
 */
public class NewStoriesResponse extends HackerNewsArrayResponse {

    public NewStoriesResponse(final String[] newStories) {
        super(newStories);
    }

    private NewStoriesResponse() {
        super();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("items", getItems())
                .toString();
    }
}
