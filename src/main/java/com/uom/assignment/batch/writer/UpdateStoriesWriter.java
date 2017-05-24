package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Story;
import com.uom.assignment.model.request.UpdateStoryModel;
import com.uom.assignment.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@link ItemWriter} that is responsible for updating any persisted {@link Story}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@Component
public class UpdateStoriesWriter implements ItemWriter<UpdateStoryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateStoriesWriter.class);

    private final StoryService storyService;

    @Autowired
    public UpdateStoriesWriter(final StoryService storyService) {
        this.storyService = storyService;
    }

    @Override
    public void write(final List<? extends UpdateStoryModel> updateStoryModels) {

        // update the deleted stories
        updateStoryModels.stream()
                         .filter(UpdateStoryModel::isDeleted)
                         .map(updateStoryModel -> storyService.delete(updateStoryModel.getStory()))
                         .forEach(updatedStory -> LOG.info("Updated Story {} to deleted", updatedStory.getHackerNewsId()));

        // update the score of the remaining stories
        updateStoryModels.stream()
                         .filter(updateStoryModel -> !updateStoryModel.isDeleted())
                         .map(updateStoryModel -> storyService.update(updateStoryModel.getStory(), updateStoryModel.getScore()))
                         .forEach(updatedStory -> LOG.info("Updated Story {} to score {}", updatedStory.getHackerNewsId(), updatedStory.getScore()));
    }
}
