package com.uom.assignment.hacker.news.api.request;

import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;

import java.util.Objects;

/**
 * The abstract {@link HackerNewsRequest} dictating the default behavior for a requested submitted to the Hacker News API.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
public abstract class AbstractHackerNewsRequest implements HackerNewsRequest {

    private final String endpoint;
    private final Class<? extends HackerNewsResponse> responseType;

    protected AbstractHackerNewsRequest(final String endpoint, final Class<? extends HackerNewsResponse> responseType) {
        this.endpoint = endpoint;
        this.responseType = responseType;
    }

    @Override
    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public Class<? extends HackerNewsResponse> getResponseType() {
        return responseType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AbstractHackerNewsRequest that = (AbstractHackerNewsRequest) o;
        return Objects.equals(getEndpoint(), that.getEndpoint()) &&
                Objects.equals(getResponseType(), that.getResponseType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEndpoint(), getResponseType());
    }
}
