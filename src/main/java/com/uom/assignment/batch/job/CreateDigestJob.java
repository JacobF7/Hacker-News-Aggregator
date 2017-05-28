package com.uom.assignment.batch.job;

import com.uom.assignment.batch.step.CreateDigestStep;
import com.uom.assignment.dao.Digest;
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
 * The {@link CreateDigestJob} for the Create Digest Job that is responsible for creating a {@link Digest} for every persisted {@link Topic}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Configuration
public class CreateDigestJob {

    public static final String JOB_NAME = "CREATE_DIGEST_JOB";

    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    public CreateDigestJob(final JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean(name = CreateDigestJob.JOB_NAME)
    @Autowired
    public Job jobDigest(@Qualifier(CreateDigestStep.STEP_NAME) final Step digestStep) {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(digestStep)
                .end()
                .build();
    }
}
