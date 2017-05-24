package com.uom.assignment.hacker.news.api.core;

import com.uom.assignment.hacker.news.api.request.HackerNewsRequest;
import com.uom.assignment.hacker.news.api.response.HackerNewsArrayResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The content type of the response returned by {@link HackerNewsRequest}.
 *
 * Created by jacobfalzon on 16/05/2017.
 */
public enum ResponseContentType {

    /**
     * The response is received as a {@link JSONArray}.
     */
    JSON_ARRAY,

    /**
     * The response is received as a {@link JSONObject}.
     */
    JSON_OBJECT;

    /**
     * Converts a {@link JSONArray} to a {@link JSONObject}.
     *
     * @param array The {@link String} representation of a {@link JSONArray}.
     * @return a {@link String} representation of the {@link JSONArray} converted into a {@link JSONObject}.
     */
    public static String toJSONObject(final String array) {
        return new JSONObject().put(HackerNewsArrayResponse.ITEMS_KEY, new JSONArray(array)).toString();
    }
}
