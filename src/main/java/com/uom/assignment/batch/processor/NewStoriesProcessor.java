package com.uom.assignment.batch.processor;

import com.uom.assignment.hacker.news.api.request.ItemRequest;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.hacker.news.api.service.HackerNewsApiService;
import com.uom.assignment.model.request.StoryIdModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The {@link ItemProcessor} that is responsible for transforming the {@link StoryIdModel} into an {@link ItemResponse}, by submitting an {@link ItemRequest} to the Hacker News API.
 *
 * Created by jacobfalzon on 17/05/2017.
 */
@Component
public class NewStoriesProcessor implements ItemProcessor<StoryIdModel, ItemResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(NewStoriesProcessor.class);
    static final String DEFAULT_URL = "https://news.ycombinator.com/item?id=";

    private final HackerNewsApiService hackerNewsApiService;

    @Autowired
    public NewStoriesProcessor(final HackerNewsApiService hackerNewsApiService) {
        this.hackerNewsApiService = hackerNewsApiService;
    }

    @Override
    public ItemResponse process(final StoryIdModel storyIdModel) throws IOException {

        LOG.info("Calling Hacker News API: [{}] for Story: {}", ItemRequest.class.getSimpleName(), storyIdModel.getId());

        // Retrieve the details of the story from Hacker News API
        final ItemRequest request = new ItemRequest(Long.valueOf(storyIdModel.getId()));

        final HackerNewsResponse response = hackerNewsApiService.doGet(request);

        if(response.isEmpty()) { // If the response is empty, do not process the item
            LOG.info("Received empty response from Hacker News API: [{}] for Story: {}", ItemRequest.class.getSimpleName(), storyIdModel.getId());
            return null;
        }

        final ItemResponse itemResponse = (ItemResponse) response;

        // If the URL is null, set the Response URL to DEFAULT_URL (only occurs for "Ask Hacker News" Stories)
        if(itemResponse.getUrl() == null) {
            itemResponse.setUrl(DEFAULT_URL + itemResponse.getId());
        }

        return itemResponse;
    }
}
