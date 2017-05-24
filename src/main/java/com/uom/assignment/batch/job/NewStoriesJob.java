package com.uom.assignment.batch.job;

import com.uom.assignment.batch.step.NewStoriesStep;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the New Stories Job, which is responsible for retrieving a batch of New Stories.
 *
 * Created by jacobfalzon on 17/05/2017.
 */
@Configuration
public class NewStoriesJob {

    public static final String JOB_NAME = "NEW_STORIES_JOB";

    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    public NewStoriesJob(final JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean(name = NewStoriesJob.JOB_NAME)
    @Autowired
    public Job jobNewStories(@Qualifier(NewStoriesStep.STEP_NAME) final Step step) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }
}
