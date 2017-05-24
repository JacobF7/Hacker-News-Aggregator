package com.uom.assignment.hacker.news.api.response;

import org.json.JSONArray;

/**
 * The abstract {@link HackerNewsResponse} defining the default behavior for all responses received as an atomic {@link JSONArray}.
 *
 * Created by jacobfalzon on 16/05/2017.
 */
public abstract class HackerNewsArrayResponse implements HackerNewsResponse {

    public static final String ITEMS_KEY = "items";

    private String[] items;

    protected HackerNewsArrayResponse() {

    }

    protected HackerNewsArrayResponse(final String[] items) {
        this.items = items;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(final String[] items) {
        this.items = items;
    }
}
