package com.uom.assignment.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

/**
 * A test suite for {@link UserTopic}.
 *
 * Created by jacobfalzon on 06/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTopicTest {

    @Mock
    private User mockUser;

    @Mock
    private User mockNewUser;

    @Mock
    private Topic mockTopic;

    @Mock
    private Topic mockNewTopic;

    private static final Long EFFECTIVE_FROM = Instant.now().toEpochMilli();
    private static final Long EFFECTIVE_TO = Instant.now().toEpochMilli();

    private static final Long NEW_EFFECTIVE_FROM = Instant.now().plusSeconds(10).toEpochMilli();
    private static final Long NEW_EFFECTIVE_TO = Instant.now().plusSeconds(10).toEpochMilli();

    private UserTopic userTopic;

    @Before
    public void setup() {
        userTopic = new UserTopic(mockUser, mockTopic);
        userTopic.setEffectiveFrom(EFFECTIVE_FROM);
        userTopic.setEffectiveTo(EFFECTIVE_TO);
    }

    @Test
    public void createUserTopic_containsAttributes() {

        // Verifying that the userTopic contains mockUser, mockTopic, EFFECTIVE_FROM and EFFECTIVE_TO
        Assert.assertEquals(mockUser, userTopic.getUser());
        Assert.assertEquals(mockTopic, userTopic.getTopic());
        Assert.assertEquals(EFFECTIVE_FROM, userTopic.getEffectiveFrom());
        Assert.assertEquals(EFFECTIVE_TO, userTopic.getEffectiveTo());
        Assert.assertFalse(userTopic.isEffective()); // User Topic is not effective as EFFECTIVE_TO is not null
    }

    @Test
    public void updateUser_containsNewAttributes() {

        userTopic.setUser(mockNewUser);
        userTopic.setTopic(mockNewTopic);
        userTopic.setEffectiveFrom(NEW_EFFECTIVE_FROM);
        userTopic.setEffectiveTo(NEW_EFFECTIVE_TO);

        // Verifying that the userTopic was updated to contain mockNewUser, mockNewTopic, NEW_EFFECTIVE_FROM, NEW_EFFECTIVE_TO
        Assert.assertEquals(mockNewUser, userTopic.getUser());
        Assert.assertEquals(mockNewTopic, userTopic.getTopic());
        Assert.assertEquals(NEW_EFFECTIVE_FROM, userTopic.getEffectiveFrom());
        Assert.assertEquals(NEW_EFFECTIVE_TO, userTopic.getEffectiveTo());
        Assert.assertFalse(userTopic.isEffective()); // User Topic is not effective as EFFECTIVE_TO is not null
    }

    @Test
    public void equals_sameObject_equal() {
        Assert.assertEquals(userTopic, userTopic);
    }

    @Test
    public void equals_differentObjects_sameAttributes_equal() {
        final UserTopic otherUserTopic = new UserTopic(mockUser, mockTopic);
        otherUserTopic.setEffectiveFrom(EFFECTIVE_FROM);
        otherUserTopic.setEffectiveTo(EFFECTIVE_TO);

        Assert.assertEquals(userTopic, otherUserTopic);
    }

    @Test
    public void equals_differentObjects_userIsDifferent_notEqual() {
        Assert.assertNotEquals(userTopic, new UserTopic(mockNewUser, mockTopic));
    }

    @Test
    public void equals_differentObjectType_notEqual() {
        Assert.assertNotEquals(userTopic, new Object());
    }

}
