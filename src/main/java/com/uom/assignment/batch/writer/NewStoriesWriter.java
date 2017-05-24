package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Story;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@link ItemWriter} that is responsible for persisting each {@link ItemResponse} as a {@link Story}.
 *
 * Created by jacobfalzon on 17/05/2017.
 */
@Component
public class NewStoriesWriter implements ItemWriter<ItemResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(NewStoriesWriter.class);

    private final StoryService storyService;

    @Autowired
    public NewStoriesWriter(final StoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public void write(final List<? extends ItemResponse> items) {
        items.stream()
             .map(item -> storyService.createOrUpdate(item.getId(), item.getTitle(), item.getBy(), item.getUrl(), item.getScore()))
             .map(Story::getHackerNewsId)
             .forEach(hackerNewsId -> LOG.info("Persisted Item {}", hackerNewsId));
    }
}
