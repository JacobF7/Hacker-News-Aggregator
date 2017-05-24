package com.uom.assignment.hacker.news.api.core;

/**
 * The URL endpoints used for submitted requests to the Hacker News API.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
public enum HackerNewsEndpoint {

    /**
     * The endpoint for retrieving New Stories.
     */
    NEW_STORIES(HackerNewsEndpoint.BASE_URL + "/newstories" + HackerNewsEndpoint.JSON_RESPONSE_SUFFIX),

    /**
     * The endpoint for retrieving the details on an Item, such as a Story.
     */
    ITEM(HackerNewsEndpoint.BASE_URL + "/item/");

    private final String endpoint;

    HackerNewsEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    // Hacker News Base Url
    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0";

    // Response Content Type
    public static final String JSON_RESPONSE_SUFFIX = ".json";
}
