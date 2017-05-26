package com.uom.assignment.batch.processor;

import com.uom.assignment.dao.Story;
import com.uom.assignment.hacker.news.api.request.ItemRequest;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.hacker.news.api.service.HackerNewsApiService;
import com.uom.assignment.model.request.UpdateStoryModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * The {@link ItemProcessor} that is responsible for transforming a {@link Story} into an {@link UpdateStoryModel}.
 * The updated {@link Story} is retrieved by submitting an {@link ItemRequest} to the Hacker News API and processing the given {@link ItemResponse}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@Component
public class UpdateStoriesProcessor implements ItemProcessor<Story, UpdateStoryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateStoriesProcessor.class);
    private final HackerNewsApiService hackerNewsApiService;

    @Autowired
    public UpdateStoriesProcessor(final HackerNewsApiService hackerNewsApiService) {
        this.hackerNewsApiService = hackerNewsApiService;
    }

    @Override
    public UpdateStoryModel process(final Story story) throws IOException {

        LOG.info("Calling Hacker News API: [{}] for Story: {}", ItemRequest.class.getSimpleName(), story.getHackerNewsId());

        // Retrieve the details of the story from Hacker News API
        final Long hackerNewsId = story.getHackerNewsId();
        final ItemRequest request = new ItemRequest(hackerNewsId);

        final HackerNewsResponse response = hackerNewsApiService.doGet(request);

        if(response.isEmpty()) { // If the response is empty, do not process the item
            LOG.info("Received empty response from Hacker News API: [{}] for Story: {}", ItemRequest.class.getSimpleName(), story.getHackerNewsId());
            return null;
        }

        final ItemResponse itemResponse = (ItemResponse) response;

        // Do not process any story that is NOT updated, i.e it is NOT deleted and the score has NOT changed
        if (!itemResponse.isDeleted() && Objects.equals(story.getScore(), itemResponse.getScore())) {
            LOG.info("The Story {} has not not been updated", hackerNewsId);
            return null;
        }

        return new UpdateStoryModel(story, itemResponse.getScore(), itemResponse.isDeleted());
    }

}
