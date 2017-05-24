package com.uom.assignment.batch.reader;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@link ItemReader} that is responsible for retrieving any currently persisted {@link Topic} to be updated.
 *
 * Created by jacobfalzon on 23/05/2017.
 */
@Component
@StepScope
public class UpdateTopicsReader implements ItemReader<Topic> {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateTopicsReader.class);
    private final List<Topic> topics;

    @Autowired
    public UpdateTopicsReader(final TopicService topicService) {
        LOG.info("Reading Topics from Database");

        // Get all the Topics from Database
        this.topics = topicService.findAll();
    }

    @Override
    public Topic read() throws Exception {
        return topics.isEmpty() ? null : topics.remove(0);
    }
}