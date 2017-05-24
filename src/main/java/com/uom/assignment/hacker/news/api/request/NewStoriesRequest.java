package com.uom.assignment.hacker.news.api.request;

import com.uom.assignment.hacker.news.api.response.NewStoriesResponse;
import com.uom.assignment.hacker.news.api.core.HackerNewsEndpoint;
import com.uom.assignment.hacker.news.api.core.ResponseContentType;

/**
 * The {@link HackerNewsRequest} for obtaining all New Stories.
 * Note that this request returns a response containing a {@link ResponseContentType#JSON_ARRAY}.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
public final class NewStoriesRequest extends AbstractHackerNewsRequest {

    public NewStoriesRequest() {
        super(HackerNewsEndpoint.NEW_STORIES.getEndpoint(),
              NewStoriesResponse.class);
    }

    @Override
    public ResponseContentType getResponseContentType() {
        return ResponseContentType.JSON_ARRAY;
    }
}
