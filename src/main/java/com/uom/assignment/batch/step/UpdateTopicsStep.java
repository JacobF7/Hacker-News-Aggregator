package com.uom.assignment.batch.step;

import com.uom.assignment.batch.processor.UpdateTopicsProcessor;
import com.uom.assignment.batch.reader.UpdateTopicsReader;
import com.uom.assignment.batch.writer.UpdateTopicsWriter;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.UpdateTopicModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the Update Topics Step, which serves to update the {@link Topic#topStory} of every persisted {@link Topic}.
 *
 * Created by jacobfalzon on 23/05/2017.
 */
@Configuration
public class UpdateTopicsStep {

    public static final String STEP_NAME = "UPDATE_TOPICS_STEP";

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${batch.update.stories.chunk}")
    private int chunk;

    @Autowired
    public UpdateTopicsStep(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = UpdateTopicsStep.STEP_NAME)
    @Autowired
    public Step stepNewStories(final UpdateTopicsReader reader, final UpdateTopicsProcessor processor, final UpdateTopicsWriter writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Topic, UpdateTopicModel>chunk(chunk)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();

        // TODO ADD ERROR HANDLER
    }

}
