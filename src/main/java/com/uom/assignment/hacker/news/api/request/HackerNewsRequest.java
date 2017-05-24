package com.uom.assignment.hacker.news.api.request;

import com.uom.assignment.hacker.news.api.core.ResponseContentType;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;

/**
 * The contract for a request submitted to the Hacker News API.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
public interface HackerNewsRequest {

    /**
     * Retrieves the URL of the request.
     *
     * @return a {@link String} containing the URL of the request.
     */
    String getEndpoint();

    /**
     * Retrieves the type of response returned by the request.
     *
     * @return the type of {@link HackerNewsResponse} returned by the request.
     */
    Class<? extends HackerNewsResponse> getResponseType();

    /**
     * Retrieves the content type of the response.
     *
     * @return the {@link ResponseContentType} returned by the request.
     */
    ResponseContentType getResponseContentType();
}
