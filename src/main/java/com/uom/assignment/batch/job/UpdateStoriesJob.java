package com.uom.assignment.batch.job;

import com.uom.assignment.batch.step.UpdateStoriesStep;
import com.uom.assignment.dao.Story;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@link Configuration} for the Update Stories Job, which is responsible for updating every persisted {@link Story}.
 * Note that a {@link Story} can only be updated if it is not {@link Story#deleted}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@Configuration
public class UpdateStoriesJob {

    public static final String JOB_NAME = "UPDATE_STORIES_JOB";

    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    public UpdateStoriesJob(final JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean(name = UpdateStoriesJob.JOB_NAME)
    @Autowired
    public Job jobNewStories(@Qualifier(UpdateStoriesStep.STEP_NAME) final Step step) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }
}
