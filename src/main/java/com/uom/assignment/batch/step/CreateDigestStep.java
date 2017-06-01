package com.uom.assignment.batch.step;

import com.uom.assignment.batch.processor.CreateDigestProcessor;
import com.uom.assignment.batch.reader.CreateDigestReader;
import com.uom.assignment.batch.writer.CreateDigestWriter;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.model.request.CreateDigestModel;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The {@link Configuration} for the Create Digest Step that is responsible for creating a {@link Digest} for the {@link Topic}s that every {@link User} is subscribed to.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Configuration
public class CreateDigestStep {

    public static final String STEP_NAME = "CREATE_DIGEST_STEP";

    private final StepBuilderFactory stepBuilderFactory;

    @Value("${batch.new.stories.chunk}")
    private int chunk;

    @Autowired
    public CreateDigestStep(final StepBuilderFactory stepBuilderFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = CreateDigestStep.STEP_NAME)
    @Autowired
    public Step stepNewStories(final CreateDigestReader reader, final CreateDigestProcessor processor, final CreateDigestWriter writer) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Topic, List<CreateDigestModel>>chunk(chunk)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
