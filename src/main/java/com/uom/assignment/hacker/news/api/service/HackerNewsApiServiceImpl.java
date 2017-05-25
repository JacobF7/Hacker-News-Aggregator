package com.uom.assignment.hacker.news.api.service;

import com.uom.assignment.hacker.news.api.core.ResponseContentType;
import com.uom.assignment.hacker.news.api.request.HackerNewsRequest;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The {@link Service} implementation for {@link HackerNewsApiService}.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
@Service
public class HackerNewsApiServiceImpl implements HackerNewsApiService {

    public HackerNewsResponse doGet(final HackerNewsRequest request) throws IOException {

        // fetch endpoint
        final String endpoint = request.getEndpoint();

        // send request
        final HttpURLConnection connection = (HttpURLConnection) new URL(endpoint).openConnection();
        connection.setRequestMethod(RequestMethod.GET.name());

        // consume response
        final InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
        final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        final String response = bufferedReader.lines().collect(Collectors.joining());

        // close connections
        inputStreamReader.close();
        bufferedReader.close();
        connection.disconnect();

        // TODO USE OPTIONAL HERE! and Test
        if(Objects.equals(response, "null")) {
            return null;
        }

        // produce response
        switch (request.getResponseContentType()) {

            case JSON_ARRAY:
                return new ObjectMapper().readValue(ResponseContentType.toJSONObject(response), request.getResponseType());

            case JSON_OBJECT:
                return new ObjectMapper().readValue(response, request.getResponseType());

            default:
                throw new UnsupportedOperationException(String.format("Response Content Type [%s] is not supported", request.getResponseContentType()));
        }
    }
}
