package com.uom.assignment.batch.step;

import com.uom.assignment.batch.processor.UpdateTopStoriesProcessor;
import com.uom.assignment.batch.reader.UpdateTopStoriesReader;
import com.uom.assignment.batch.writer.UpdateTopStoriesWriter;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.UpdateTopStoriesModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the Update Top Stories Step, which serves to update the {@link Topic#topStory} of every persisted {@link Topic}.
 *
 * Created by jacobfalzon on 23/05/2017.
 */
@Configuration
public class UpdateTopStoriesStep {

    public static final String STEP_NAME = "UPDATE_TOP_STORIES_STEP";

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${batch.update.stories.chunk}")
    private int chunk;

    @Autowired
    public UpdateTopStoriesStep(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = UpdateTopStoriesStep.STEP_NAME)
    @Autowired
    public Step stepNewStories(final UpdateTopStoriesReader reader, final UpdateTopStoriesProcessor processor, final UpdateTopStoriesWriter writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Topic, UpdateTopStoriesModel>chunk(chunk)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        // TODO ADD ERROR HANDLER
    }

}
