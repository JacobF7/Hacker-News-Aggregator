package com.uom.assignment.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Random;

/**
 * A test suite for {@link Story}.
 *
 * Created by jacobfalzon on 06/06/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class StoryTest {

    @Mock
    private Digest mockDigest;

    @Mock
    private Digest mockNewDigest;

    private static final Random RANDOM = new Random();

    private static final Long HACKER_NEWS_ID = RANDOM.nextLong();
    private static final String TITLE = "TITLE";
    private static final String AUTHOR = "AUTHOR";
    private static final String URL = "URL";
    private static final Long SCORE = RANDOM.nextLong();
    private static final Boolean IS_DELETED = false;
    private static final Long LAST_UPDATED = System.currentTimeMillis();
    private static final Long CREATION_DATE = System.currentTimeMillis();

    private static final Long NEW_HACKER_NEWS_ID = RANDOM.nextLong();
    private static final String NEW_TITLE = "NEW_TITLE";
    private static final String NEW_AUTHOR = "NEW_AUTHOR";
    private static final String NEW_URL = "NEW_URL";
    private static final Long NEW_SCORE = RANDOM.nextLong();
    private static final Boolean NEW_IS_DELETED = true;
    private static final Long NEW_LAST_UPDATED = System.currentTimeMillis();
    private static final Long NEW_CREATION_DATE = System.currentTimeMillis();

    private Story story;

    @Before
    public void setup() {
        story = new Story(HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, CREATION_DATE);
        story.setLastUpdated(LAST_UPDATED);
        story.setDigests(Collections.singletonList(mockDigest));
    }

    @Test
    public void createStory_containsAttributes() {

        // Verifying that the digest contains HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, IS_DELETED, LAST_UPDATED, CREATION_DATE and mockDigest
        Assert.assertEquals(HACKER_NEWS_ID, story.getHackerNewsId());
        Assert.assertEquals(TITLE, story.getTitle());
        Assert.assertEquals(AUTHOR, story.getAuthor());
        Assert.assertEquals(URL, story.getUrl());
        Assert.assertEquals(SCORE, story.getScore());
        Assert.assertEquals(IS_DELETED, story.isDeleted());
        Assert.assertEquals(!IS_DELETED, story.isActive());
        Assert.assertEquals(LAST_UPDATED, story.getLastUpdated());
        Assert.assertEquals(CREATION_DATE, story.getCreationDate());
        Assert.assertEquals(Collections.singletonList(mockDigest), story.getDigests());
    }

    @Test
    public void updateStory_containsNewAttributes() {

        story.setHackerNewsId(NEW_HACKER_NEWS_ID);
        story.setTitle(NEW_TITLE);
        story.setAuthor(NEW_AUTHOR);
        story.setUrl(NEW_URL);
        story.setScore(NEW_SCORE);
        story.setDeleted(NEW_IS_DELETED);
        story.setLastUpdated(NEW_LAST_UPDATED);
        story.setCreationDate(NEW_CREATION_DATE);
        story.setDigests(Collections.singletonList(mockNewDigest));

        // Verifying that the story was updated to contain NEW_HACKER_NEWS_ID, NEW_TITLE, NEW_AUTHOR, NEW_URL, NEW_SCORE , NEW_IS_DELETED, NEW_LAST_UPDATED, NEW_CREATION_DATE and mockNewDigest
        Assert.assertEquals(NEW_HACKER_NEWS_ID, story.getHackerNewsId());
        Assert.assertEquals(NEW_TITLE, story.getTitle());
        Assert.assertEquals(NEW_AUTHOR, story.getAuthor());
        Assert.assertEquals(NEW_URL, story.getUrl());
        Assert.assertEquals(NEW_SCORE, story.getScore());
        Assert.assertEquals(NEW_IS_DELETED, story.isDeleted());
        Assert.assertEquals(NEW_LAST_UPDATED, story.getLastUpdated());
        Assert.assertEquals(NEW_CREATION_DATE, story.getCreationDate());
        Assert.assertEquals(Collections.singletonList(mockNewDigest), story.getDigests());
    }

    @Test
    public void equals_sameObject_equal() {
        Assert.assertEquals(story, story);
    }

    @Test
    public void equals_differentObjects_sameAttributes_equal() {

        final Story otherStory = new Story(HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, CREATION_DATE);
        otherStory.setLastUpdated(LAST_UPDATED);
        otherStory.setDigests(Collections.singletonList(mockDigest));

        Assert.assertEquals(story, otherStory);
    }

    @Test
    public void equals_differentObjects_scoreIsDifferent_notEqual() {

        final Story otherStory = new Story(HACKER_NEWS_ID, TITLE, AUTHOR, URL, NEW_SCORE, CREATION_DATE);
        otherStory.setDigests(Collections.singletonList(mockDigest));

        Assert.assertNotEquals(story, otherStory);
    }

    @Test
    public void equals_differentObjectType_notEqual() {
        Assert.assertNotEquals(story, new Object());
    }

    @Test
    public void toString_containsAttributes() {

        final String storyRepresentation = story.toString();

        Assert.assertTrue(storyRepresentation.contains(HACKER_NEWS_ID.toString()));
        Assert.assertTrue(storyRepresentation.contains(TITLE));
        Assert.assertTrue(storyRepresentation.contains(AUTHOR));
        Assert.assertTrue(storyRepresentation.contains(URL));
        Assert.assertTrue(storyRepresentation.contains(SCORE.toString()));
        Assert.assertTrue(storyRepresentation.contains(IS_DELETED.toString()));
        Assert.assertTrue(storyRepresentation.contains(LAST_UPDATED.toString()));
        Assert.assertTrue(storyRepresentation.contains(CREATION_DATE.toString()));
    }
}
