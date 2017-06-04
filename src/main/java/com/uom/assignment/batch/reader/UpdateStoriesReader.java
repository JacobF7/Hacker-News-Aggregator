package com.uom.assignment.batch.reader;

import com.uom.assignment.dao.Story;
import com.uom.assignment.service.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ItemReader} that is responsible for retrieving any currently persisted {@link Story} to be updated.
 * Note that a {@link Story} can only be updated if it is not {@link Story#deleted}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@Component
@StepScope
public class UpdateStoriesReader implements ItemReader<Story> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateStoriesReader.class);
    private final List<Story> stories;

    @Autowired
    public UpdateStoriesReader(final StoryService storyService,
                               @Value("#{jobParameters[T(com.uom.assignment.batch.reader.FetchMode).FETCH_MODE]}") final FetchMode fetchMode,
                               @Value("${story.recent.duration.hours}") int duration) {

        LOG.info("Reading [{}] Stories from Database", fetchMode);

        switch (fetchMode) {
            case ALL:
                stories = new ArrayList<>(storyService.findAllActive());
                break;

            case RECENT:
                stories = new ArrayList<>(storyService.findActiveByLastUpdatedDuration(Duration.ofHours(duration)));
                break;

            default:
                stories = new ArrayList<>(); // default is an empty list
                break;
        }
    }

    @Override
    public Story read() {
        return stories.isEmpty() ? null : stories.remove(0);
    }

}
