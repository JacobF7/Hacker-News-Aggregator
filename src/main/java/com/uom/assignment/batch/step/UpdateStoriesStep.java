package com.uom.assignment.batch.step;

import com.uom.assignment.batch.processor.UpdateStoriesProcessor;
import com.uom.assignment.batch.reader.UpdateStoriesReader;
import com.uom.assignment.batch.writer.UpdateStoriesWriter;
import com.uom.assignment.dao.Story;
import com.uom.assignment.model.request.UpdateStoryModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the Update Stories Step, which serves to update every persisted {@link Story}.
 * Note that a {@link Story} can only be updated if it is not {@link Story#deleted}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@Configuration
public class UpdateStoriesStep {

    public static final String STEP_NAME = "UPDATE_STORIES_STEP";

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${batch.update.stories.chunk}")
    private int chunk;

    @Autowired
    public UpdateStoriesStep(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = UpdateStoriesStep.STEP_NAME)
    @Autowired
    public Step stepNewStories(final UpdateStoriesReader reader, final UpdateStoriesProcessor processor, final UpdateStoriesWriter writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Story, UpdateStoryModel>chunk(chunk)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
