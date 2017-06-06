package com.uom.assignment.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collections;

/**
 * A test suite for {@link Digest}
 *
 * Created by jacobfalzon on 06/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class DigestTest {

    @Mock
    private Topic  mockTopic;

    @Mock
    private Topic  newMockTopic;

    @Mock
    private Story mockStory;

    @Mock
    private Story newMockStory;

    @Mock
    private User mockUser;

    @Mock
    private User newMockUser;

    private static final Long CREATION_DATE = Instant.now().toEpochMilli();
    private static final Boolean IS_OVERALL = false;

    private static final Long NEW_CREATION_DATE = Instant.now().plusSeconds(10).toEpochMilli();
    private static final Boolean NEW_IS_OVERALL = true;

    private static final String TITLE = "TITLE";
    private static final String TOPIC = "TOPIC";
    private static final Long ID = new SecureRandom().nextLong();

    private Digest digest;

    @Before
    public void setup() {

        digest = new Digest(mockTopic, mockStory, CREATION_DATE, Collections.singleton(mockUser), IS_OVERALL);

        // Mocking that mockStory contains TOPIC
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC);

        // Mocking that mockStory contains TITLE
        Mockito.when(mockStory.getTitle()).thenReturn(TITLE);

        // Mocking that mockUser has ID
        Mockito.when(mockUser.getId()).thenReturn(ID);
    }

    @Test
    public void createDigest_containsAttributes() {

        // Verifying that the digest contains mockTopic, mockStory, CREATION_DATE, mockUser and IS_OVERALL
        Assert.assertEquals(mockTopic, digest.getTopic());
        Assert.assertEquals(mockStory, digest.getStory());
        Assert.assertEquals(CREATION_DATE, digest.getCreationDate());
        Assert.assertEquals(Collections.singleton(mockUser), digest.getUsers());
        Assert.assertEquals(IS_OVERALL, digest.getOverall());
    }

    @Test
    public void updateDigest_containsNewAttributes() {

        digest.setTopic(newMockTopic);
        digest.setStory(newMockStory);
        digest.setCreationDate(NEW_CREATION_DATE);
        digest.setUsers(Collections.singleton(newMockUser));
        digest.setOverall(NEW_IS_OVERALL);

        // Verifying that the digest was updated to contain newMockTopic, newMockStory, NEW_CREATION_DATE, newMockUser and NEW_IS_OVERALL
        Assert.assertEquals(newMockTopic, digest.getTopic());
        Assert.assertEquals(newMockStory, digest.getStory());
        Assert.assertEquals(NEW_CREATION_DATE, digest.getCreationDate());
        Assert.assertEquals(Collections.singleton(newMockUser), digest.getUsers());
        Assert.assertEquals(NEW_IS_OVERALL, digest.getOverall());
    }

    @Test
    public void equals_sameObject_equal() {
        Assert.assertEquals(digest, digest);
    }

    @Test
    public void equals_differentObjects_sameAttributes_equal() {
        Assert.assertEquals(digest, new Digest(mockTopic, mockStory, CREATION_DATE, Collections.singleton(mockUser), IS_OVERALL));
    }

    @Test
    public void equals_differentObjects_overallIsDifferent_notEqual() {
        Assert.assertNotEquals(digest, new Digest(mockTopic, mockStory, CREATION_DATE, Collections.singleton(mockUser), NEW_IS_OVERALL));
    }

    @Test
    public void equals_differentObjectType_notEqual() {
        Assert.assertNotEquals(digest, new Object());
    }

    @Test
    public void toString_containsAttributes() {

        final String digestRepresentation = digest.toString();

        Assert.assertTrue(digestRepresentation.contains(TOPIC));
        Assert.assertTrue(digestRepresentation.contains(TITLE));
        Assert.assertTrue(digestRepresentation.contains(CREATION_DATE.toString()));
        Assert.assertTrue(digestRepresentation.contains(mockUser.getId().toString()));
        Assert.assertTrue(digestRepresentation.contains(IS_OVERALL.toString()));
    }

}
