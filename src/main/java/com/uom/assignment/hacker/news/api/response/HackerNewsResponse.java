package com.uom.assignment.hacker.news.api.response;

/**
 * A marker interface for all responses received from the Hacker News API.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
public interface HackerNewsResponse {

    /**
     * Should return true if the response is empty, false otherwise.
     *
     * @return false, since by default a response is not empty.
     */
    default Boolean isEmpty() {
        return false;
    }
}
