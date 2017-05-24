package com.uom.assignment.service;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.repository.TopicRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * A test suite for {@link TopicService}.
 *
 * Created by jacobfalzon on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TopicServiceImplTest {

    @Mock
    private Topic mockTopic;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

    private static final String TOPIC_NAME = "topic";

    @Before
    public void setup() {
        // Mocking mockTopic to contain TOPIC_NAME
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);
    }

    @Test
    public void findByName_returnsTopic() {

        Mockito.when(topicRepository.findByName(TOPIC_NAME)).thenReturn(Optional.of(mockTopic));

        Assert.assertEquals(Optional.of(mockTopic), topicService.findByName(TOPIC_NAME));
    }

    @Test
    public void create_topicsDoesNotExist_createsTopic() {
        // Mocking that there no Topic exists with TOPIC_NAME
        Mockito.when(topicRepository.findByName(TOPIC_NAME)).thenReturn(Optional.empty());

        // Mocking the persisted Topic
        final Topic savedTopic = new Topic(TOPIC_NAME);
        Mockito.when(topicRepository.save(Mockito.any(Topic.class))).thenReturn(savedTopic);

        final Topic topic = topicService.create(TOPIC_NAME);

        // Verifying that a new topic was saved with TOPIC_NAME
        final ArgumentCaptor<Topic> topicArgumentCaptor = ArgumentCaptor.forClass(Topic.class);
        Mockito.verify(topicRepository).save(topicArgumentCaptor.capture());
        Assert.assertEquals(TOPIC_NAME, topicArgumentCaptor.getValue().getName());

        // Verifying that a new topic was created with TOPIC_NAME.
        Assert.assertEquals(topic, savedTopic);
    }

    @Test
    public void createTopics_topicsExists_fetchesTopic() {
        // Mocking that a Topic exists with TOPIC_NAME
        Mockito.when(topicRepository.findByName(TOPIC_NAME)).thenReturn(Optional.of(mockTopic));

        final Topic topic = topicService.create(TOPIC_NAME);

        // Verifying that a new topic was NOT created
        Mockito.verify(topicRepository, Mockito.never()).save(Mockito.any(Topic.class));

        // Verifying that the topic was fetched
        Assert.assertEquals(topic, mockTopic);
    }
}
