package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DigestService;
import com.uom.assignment.service.DurationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The {@link ItemWriter} that is responsible for persisting a {@link Digest} for the top {@link Story} of every {@link Topic}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Component
public class CreateDigestWriter implements ItemWriter<TopStoryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDigestWriter.class);

    private final DigestService digestService;

    @Autowired
    public CreateDigestWriter(final DigestService digestService) {
        this.digestService = digestService;
    }

    @Override
    public void write(final List<? extends TopStoryModel> topStoryModels) {
        topStoryModels.stream()
                      .map(topStoryModel -> digestService.create(topStoryModel.getTopic(), topStoryModel.getStory(), DurationType.WEEKLY))
                      .forEach(digest -> LOG.info("Created Digest: {}", digest));
    }
}
