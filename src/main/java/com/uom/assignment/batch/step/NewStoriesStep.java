package com.uom.assignment.batch.step;

import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.batch.processor.NewStoriesProcessor;
import com.uom.assignment.batch.reader.NewStoriesReader;
import com.uom.assignment.batch.writer.NewStoriesWriter;
import com.uom.assignment.model.request.StoryIdModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the New Stories Step, which fetches a batch of New Stories from the Hacker News API, transforms the response and subsequently persists it.
 *
 * Created by jacobfalzon on 17/05/2017.
 */
@Configuration
public class NewStoriesStep {

    public static final String STEP_NAME = "NEW_STORIES_STEP";

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${batch.new.stories.chunk}")
    private int chunk;

    @Autowired
    public NewStoriesStep(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = NewStoriesStep.STEP_NAME)
    @Autowired
    public Step stepNewStories(final NewStoriesReader reader, final NewStoriesProcessor processor, final NewStoriesWriter writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<StoryIdModel, ItemResponse>chunk(chunk)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
