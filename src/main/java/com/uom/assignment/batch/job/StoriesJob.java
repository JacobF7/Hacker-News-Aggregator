package com.uom.assignment.batch.job;

import com.uom.assignment.batch.step.NewStoriesStep;
import com.uom.assignment.batch.step.UpdateStoriesStep;
import com.uom.assignment.batch.step.UpdateTopicsStep;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the Stories Job, which is responsible for:
 *      a) Updating every persisted {@link Story}, since the {@link Story#score} and {@link Story#deleted} may have changed.
 *      b) Retrieving a batch of new Stories and persisting every new {@link Story}.
 *      c) Updating every persisted {@link Topic}, since the {@link Topic#topStory} may have changed.
 *
 * Created by jacobfalzon on 24/05/2017.
 */
@Configuration
public class StoriesJob {

    public static final String JOB_NAME = "STORIES_JOB";

    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StoriesJob(final JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean(name = NewStoriesJob.JOB_NAME)
    @Autowired
    public Job jobNewStories(@Qualifier(UpdateStoriesStep.STEP_NAME) final Step updateStoriesStep,
                             @Qualifier(NewStoriesStep.STEP_NAME) final Step newStoriesStep,
                             @Qualifier(UpdateTopicsStep.STEP_NAME) final Step updateTopicsStep) {

        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(updateStoriesStep)
                .next(newStoriesStep)
                .next(updateTopicsStep)
                .end()
                .build();
    }

}
