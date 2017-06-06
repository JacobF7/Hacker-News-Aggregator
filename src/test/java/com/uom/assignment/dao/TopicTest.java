package com.uom.assignment.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * A test suite for {@link Topic}.
 *
 * Created by jacobfalzon on 06/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class TopicTest {

    @Mock
    private UserTopic mockUserTopic;

    @Mock
    private UserTopic mockNewUserTopic;

    @Mock
    private Story mockTopStory;

    @Mock
    private Story mockNewTopStory;

    private static final String TOPIC_NAME = "TOPIC";
    private static final String NEW_TOPIC_NAME = "NEWTOPIC";

    private Topic topic;

    @Before
    public void setup() {
        topic = new Topic(TOPIC_NAME, mockTopStory);
        topic.setUserTopics(Collections.singleton(mockUserTopic));
    }

    @Test
    public void createTopic_containsAttributes() {

        // Verifying that the topic contains TOPIC_NAME, mockTopStory and mockUserTopic
        Assert.assertEquals(TOPIC_NAME, topic.getName());
        Assert.assertEquals(mockTopStory, topic.getTopStory());
        Assert.assertEquals(Collections.singleton(mockUserTopic), topic.getUserTopics());
    }

    @Test
    public void updateTopic_containsNewAttributes() {

        topic.setName(NEW_TOPIC_NAME);
        topic.setTopStory(mockNewTopStory);
        topic.setUserTopics(Collections.singleton(mockNewUserTopic));

        // Verifying that the topic was updated to contain NEW_TOPIC_NAME, mockNewTopStory and mockNewUserTopic
        Assert.assertEquals(NEW_TOPIC_NAME, topic.getName());
        Assert.assertEquals(mockNewTopStory, topic.getTopStory());
        Assert.assertEquals(Collections.singleton(mockNewUserTopic), topic.getUserTopics());
    }

    @Test
    public void equals_sameObject_equal() {
        Assert.assertEquals(topic, topic);
    }

    @Test
    public void equals_differentObjects_sameAttributes_equal() {
        final Topic otherTopic = new Topic(TOPIC_NAME, mockTopStory);
        otherTopic.setUserTopics(Collections.singleton(mockUserTopic));

        Assert.assertEquals(topic, otherTopic);
    }

    @Test
    public void equals_differentObjects_topicNameIsDifferent_notEqual() {
        Assert.assertNotEquals(topic, new Topic(NEW_TOPIC_NAME, mockTopStory));
    }

    @Test
    public void equals_differentObjectType_notEqual() {
        Assert.assertNotEquals(topic, new Object());
    }

}
