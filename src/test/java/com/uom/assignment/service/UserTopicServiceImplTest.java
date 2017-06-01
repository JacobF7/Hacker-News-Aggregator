package com.uom.assignment.service;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.dao.UserTopic;
import com.uom.assignment.repository.UserTopicRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Set;

/**
 * A test suite for {@link UserTopicService}.
 *
 * Created by jacobfalzon on 12/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTopicServiceImplTest {

    @Mock
    private Topic oldTopic;

    @Mock
    private Topic newTopic;

    @Mock
    private Topic mockTopic;

    @Mock
    private User mockUser;

    @Mock
    private UserTopic oldUserTopic;

    @Mock
    private UserTopic mockUserTopic;

    @Mock
    private UserTopicRepository userTopicRepository;

    @InjectMocks
    private UserTopicServiceImpl userTopicService;

    @Before
    public void setup() {

        // Mocking that mockUser is already subscribed to oldTopic, i.e. oldUserTopic
        Mockito.when(oldUserTopic.getUser()).thenReturn(mockUser);
        Mockito.when(oldUserTopic.getTopic()).thenReturn(oldTopic);
        Mockito.when(mockUser.getUserTopics()).thenReturn(Collections.singleton(oldUserTopic));
    }

    @Test
    public void subscribe_expireEffectiveUserTopic_createNewUserTopic() {

        // Mocking oldUserTopic to be effective
        Mockito.when(mockUser.getEffectiveUserTopics()).thenReturn(Collections.singleton(oldUserTopic));

        userTopicService.subscribe(mockUser, Collections.singleton(newTopic));

        // Verifying that the oldUserTopic is expired, i.e. mockUser is no longer subscribed to oldTopic
        Mockito.verify(oldUserTopic).setEffectiveTo(Mockito.anyLong());

        // Verifying that a new UserTopic was created
        Mockito.verify(userTopicRepository).save(Mockito.any(UserTopic.class));
    }

    @Test
    public void subscribe_userTopicExists_userTopicEffective_doNothing() {

        // Mocking oldUserTopic to be effective
        Mockito.when(mockUser.getEffectiveUserTopics()).thenReturn(Collections.singleton(oldUserTopic));

        userTopicService.subscribe(mockUser, Collections.singleton(oldTopic));

        // Verifying that the oldUserTopic is still active, i.e. mockUser is still subscribed to oldTopic
        Mockito.verify(oldUserTopic, Mockito.never()).setEffectiveTo(Mockito.anyLong());

        // Verifying that a new UserTopic was not created
        Mockito.verify(userTopicRepository, Mockito.never()).save(Mockito.any(UserTopic.class));
    }

    @Test
    public void subscribe_userTopicExists_userTopicIneffective_createNewUserTopic() {

        // Mocking oldUserTopic to be ineffective
        Mockito.when(mockUser.getEffectiveUserTopics()).thenReturn(Collections.emptySet());

        userTopicService.subscribe(mockUser, Collections.singleton(oldTopic));

        // Verifying that the ineffective oldUserTopic was not expired again
        Mockito.verify(oldUserTopic, Mockito.never()).setEffectiveTo(Mockito.anyLong());

        // Verifying that a new UserTopic was created
        Mockito.verify(userTopicRepository).save(Mockito.any(UserTopic.class));
    }

    @Test
    public void findEffectiveByTopic_returnsUserTopics() {

        // Mocking that mockUserTopic is returned
        Mockito.when(userTopicRepository.findByTopicAndEffectiveToIsNull(mockTopic)).thenReturn(Collections.singleton(mockUserTopic));

        final Set<UserTopic> userTopics = userTopicService.findEffectiveByTopic(mockTopic);

        // Verifying that an attempt was made to retrieve the effective user topics by topic name
        Mockito.verify(userTopicRepository).findByTopicAndEffectiveToIsNull(mockTopic);

        // Verifying that mockUserTopic was returned
        Assert.assertEquals(Collections.singleton(mockUserTopic), userTopics);
    }
}
