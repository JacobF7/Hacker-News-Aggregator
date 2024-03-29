package com.uom.assignment.batch.reader;

import com.uom.assignment.hacker.news.api.request.NewStoriesRequest;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;
import com.uom.assignment.hacker.news.api.response.NewStoriesResponse;
import com.uom.assignment.hacker.news.api.service.HackerNewsApiService;
import com.uom.assignment.model.request.StoryIdModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@link ItemReader} that is responsible for submitting a {@link NewStoriesRequest} to the Hacker News API, as well as consuming the {@link NewStoriesResponse} obtained.
 * The {@link ItemReader} transforms each story in the {@link NewStoriesResponse} to a {@link StoryIdModel}.
 *
 * Created by jacobfalzon on 15/05/2017.
 */
@Component
@StepScope
public class NewStoriesReader implements ItemReader<StoryIdModel> {

    private static final Logger LOG = LoggerFactory.getLogger(NewStoriesReader.class);
    private final List<StoryIdModel> newStories;

    @Autowired
    public NewStoriesReader(final HackerNewsApiService hackerNewsApiService) throws IOException {
        LOG.info("Calling Hacker News API: [{}]", NewStoriesRequest.class.getSimpleName());

        // Get all the New Stories from Hacker News API
        final HackerNewsResponse response = hackerNewsApiService.doGet(new NewStoriesRequest());

        if(response.isEmpty()) { // If the response is empty, we assume that no stories are received
            LOG.info("Received empty response from Hacker News API : [{}]", NewStoriesRequest.class.getSimpleName());
            newStories = new ArrayList<>();
        } else {
            final NewStoriesResponse newStoriesResponse = (NewStoriesResponse) response;
            newStories = Arrays.stream(newStoriesResponse.getItems()).map(StoryIdModel::new).collect(Collectors.toList());
        }
    }

    @Override
    public StoryIdModel read() {
        return newStories.isEmpty() ? null : newStories.remove(0);
    }
}
