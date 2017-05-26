package com.uom.assignment.hacker.news.api.response;

/**
 * The {@link HackerNewsResponse} for an empty response, i.e. {@code null}.
 *
 * Created by jacobfalzon on 26/05/2017.
 */
public class EmptyResponse implements HackerNewsResponse {

    private static final String EMPTY_RESPONSE = "null";

    @Override
    public Boolean isEmpty() {
        return true;
    }

    public static boolean isEmptyResponse(final String response) {
        return response.equals(EMPTY_RESPONSE);
    }
}
