package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.*;
import com.uom.assignment.model.request.CreateDigestModel;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DigestService;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import com.uom.assignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link ItemWriter} that is responsible for persisting a {@link Digest} for the {@link Topic}s that every {@link User} is subscribed to.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@Component
public class CreateDigestWriter implements ItemWriter<List<CreateDigestModel>> {

    private static final Logger LOG = LoggerFactory.getLogger(CreateDigestWriter.class);

    private final DigestService digestService;

    @Autowired
    public CreateDigestWriter(final DigestService digestService) {
        this.digestService = digestService;
    }

    @Override
    public void write(final List<? extends List<CreateDigestModel>> createDigestModels) {

        // Create the digests per topic and subscribed users
        createDigestModels.stream()
                          .flatMap(List::stream)
                          .map(createDigestModel -> digestService.createTopicDigest(createDigestModel.getTopic(), createDigestModel.getStory(), createDigestModel.getUsers(), DurationType.WEEKLY))
                          .forEach(digest -> LOG.info("Created Topic Digest: {}", digest));
    }
}
