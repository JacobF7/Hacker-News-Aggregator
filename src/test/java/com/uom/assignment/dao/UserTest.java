package com.uom.assignment.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * A test suite for {@link User}.
 *
 * Created by jacobfalzon on 06/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    @Mock
    private Session mockSession;

    @Mock
    private Session mockNewSession;

    @Mock
    private UserTopic mockUserTopic;

    @Mock
    private UserTopic mockNewUserTopic;

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    private static final String NEW_USERNAME = "NEW_USERNAME";
    private static final String NEW_PASSWORD = "NEW_PASSWORD";

    private User user;

    @Before
    public void setup() {
        user = new User(USERNAME, PASSWORD);
        user.setSessions(Collections.singleton(mockSession));
        user.setUserTopics(Collections.singleton(mockUserTopic));
    }

    @Test
    public void createUser_containsAttributes() {

        // Mocking that mockUserTopic is effective
        Mockito.when(mockUserTopic.isEffective()).thenReturn(true);

        // Verifying that the user contains USERNAME, PASSWORD, mockSession and mockUserTopic
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(PASSWORD, user.getPassword());
        Assert.assertEquals(Collections.singleton(mockSession), user.getSessions());
        Assert.assertEquals(Collections.singleton(mockUserTopic), user.getUserTopics());
        Assert.assertEquals(Collections.singleton(mockUserTopic), user.getEffectiveUserTopics()); // mockUserTopic is also effective
    }

    @Test
    public void updateUser_containsNewAttributes() {

        user.setUsername(NEW_USERNAME);
        user.setPassword(NEW_PASSWORD);
        user.setSessions(Collections.singleton(mockNewSession));
        user.setUserTopics(Collections.singleton(mockNewUserTopic));

        // Verifying that the user was updated to contain NEW_USERNAME, NEW_PASSWORD, mockNewSession and mockUserTopic
        Assert.assertEquals(NEW_USERNAME, user.getUsername());
        Assert.assertEquals(NEW_PASSWORD, user.getPassword());
        Assert.assertEquals(Collections.singleton(mockNewSession), user.getSessions());
        Assert.assertEquals(Collections.singleton(mockNewUserTopic), user.getUserTopics());
    }

    @Test
    public void equals_sameObject_equal() {
        Assert.assertEquals(user, user);
    }

    @Test
    public void equals_differentObjects_sameAttributes_equal() {
        final User otherUser = new User(USERNAME, PASSWORD);
        user.setSessions(Collections.singleton(mockSession));
        user.setUserTopics(Collections.singleton(mockUserTopic));

        Assert.assertEquals(user, otherUser);
    }

    @Test
    public void equals_differentObjects_usernameIsDifferent_notEqual() {
        Assert.assertNotEquals(user, new User(NEW_USERNAME, PASSWORD));
    }

    @Test
    public void equals_differentObjectType_notEqual() {
        Assert.assertNotEquals(user, new Object());
    }

}
