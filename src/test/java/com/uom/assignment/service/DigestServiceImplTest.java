package com.uom.assignment.service;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.repository.DigestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for {@link DigestService}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DigestServiceImplTest {

    @Mock
    private Topic mockTopic;

    @Mock
    private Story mockStory;

    @Mock
    private DigestRepository digestRepository;

    @Mock
    private StoryService storyService;

    @InjectMocks
    private DigestServiceImpl digestService;

    private static final String TOPIC_NAME = "topic";

    @Test
    public void create_topStoryExists_persistsDigest() {

        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        digestService.create(mockTopic, mockStory, DurationType.WEEKLY);

        final ArgumentCaptor<Digest> argumentCaptor = ArgumentCaptor.forClass(Digest.class);

        // Verifying that a digest was created
        Mockito.verify(digestRepository).save(argumentCaptor.capture());

        final Digest digest = argumentCaptor.getValue();

        // Verifying that that mockTopic and mockStory
        Assert.assertEquals(mockTopic, digest.getTopic());
        Assert.assertEquals(mockStory, digest.getStory());
    }
}
