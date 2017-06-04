package com.uom.assignment.batch.reader;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link ItemReader} that is responsible for retrieving any persisted {@link Topic} for which a {@link Digest} is to be created.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Component
@StepScope
public class CreateDigestReader implements ItemReader<Topic> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateTopStoriesReader.class);
    private final List<Topic> topics;

    @Autowired
    public CreateDigestReader(final TopicService topicService) {
        LOG.info("Reading Topics from Database");

        // Get all the Topics from Database
        this.topics = new ArrayList<>(topicService.findAll());
    }

    @Override
    public Topic read() {
        return topics.isEmpty() ? null : topics.remove(0);
    }
}
