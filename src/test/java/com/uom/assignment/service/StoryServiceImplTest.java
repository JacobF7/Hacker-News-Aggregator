package com.uom.assignment.service;

import com.uom.assignment.dao.Story;
import com.uom.assignment.repository.StoryRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Duration;
import java.util.*;

/**
 * A test suite for {@link StoryService}.
 *
 * Created by jacobfalzon on 20/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class StoryServiceImplTest {

    @Mock
    private Story mockStory;

    @Mock
    private Story mockTopStory;

    @Mock
    private Story mockAverageStory;

    @Mock
    private StoryRepository storyRepository;

    @InjectMocks
    private StoryServiceImpl storyService;

    private static final Random RANDOM = new Random();

    private static final Long HACKER_NEWS_ID = RANDOM.nextLong();
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String URL = "url";
    private static final Long SCORE = RANDOM.nextLong();
    private static final Duration DURATION = Duration.ofMillis(RANDOM.nextLong());
    private static final String TOPIC = "topic";
    private static final Long LOW_SCORE = 0L;
    private static final Long AVERAGE_SCORE = 5L;
    private static final Long TOP_SCORE = 10L;
    private static final Long CREATION_DATE = RANDOM.nextLong();
    private static final Long STORY_ID = RANDOM.nextLong();

    @Test
    public void findByHackerNewsId_returnsStory() {
        Mockito.when(storyRepository.findByHackerNewsId(HACKER_NEWS_ID)).thenReturn(Optional.of(mockStory));

        Assert.assertEquals(Optional.of(mockStory), storyService.findByHackerNewsId(HACKER_NEWS_ID));
    }

    @Test
    public void createOrUpdate_storyDoesNotExist_createsStory() {
        // Mocking that no Story does not exist with HACKER_NEWS_ID
        Mockito.when(storyRepository.findByHackerNewsId(HACKER_NEWS_ID)).thenReturn(Optional.empty());

        storyService.createOrUpdate(HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, CREATION_DATE);

        final ArgumentCaptor<Story> argumentCaptor = ArgumentCaptor.forClass(Story.class);

        // Verifying that a new Story was actually created and saved
        Mockito.verify(storyRepository).save(argumentCaptor.capture());

        final Story story = argumentCaptor.getValue();

        // Verifying that the Story was created with HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, CREATION_DATE
        Assert.assertEquals(HACKER_NEWS_ID, story.getHackerNewsId());
        Assert.assertEquals(TITLE, story.getTitle());
        Assert.assertEquals(AUTHOR, story.getAuthor());
        Assert.assertEquals(URL, story.getUrl());
        Assert.assertEquals(SCORE, story.getScore());
        Assert.assertEquals(CREATION_DATE, story.getCreationDate());
    }

    @Test
    public void createOrUpdate_storyExists_updatesStory() {
        // Mocking that a Story exists with HACKER_NEWS_ID
        Mockito.when(storyRepository.findByHackerNewsId(HACKER_NEWS_ID)).thenReturn(Optional.of(mockStory));

        storyService.createOrUpdate(HACKER_NEWS_ID, TITLE, AUTHOR, URL, SCORE, CREATION_DATE);

        // Verifying that the score for mockStory was set to SCORE
        Mockito.verify(mockStory).setScore(SCORE);

        // Verifying that the lastUpdated for mockStory was set
        Mockito.verify(mockStory).setLastUpdated(Matchers.anyLong());

        // Verifying that the story was saved
        Mockito.verify(storyRepository).save(mockStory);
    }

    @Test
    public void update_setsScore_setsLastUpdated() {
        storyService.update(mockStory, SCORE);

        // Verifying that the score for mockStory was set to SCORE
        Mockito.verify(mockStory).setScore(SCORE);

        // Verifying that the lastUpdated for mockStory was set
        Mockito.verify(mockStory).setLastUpdated(Matchers.anyLong());

        // Verifying that the mockStory was updated
        Mockito.verify(storyRepository).save(mockStory);
    }

    @Test
    public void delete_setsDeleted_setsLastUpdated() {
        storyService.delete(mockStory);

        // Verifying that deleted for mockStory was set to true
        Mockito.verify(mockStory).setDeleted(true);

        // Verifying that the lastUpdated for mockStory was set
        Mockito.verify(mockStory).setLastUpdated(Matchers.anyLong());

        // Verifying that the mockStory was updated
        Mockito.verify(storyRepository).save(mockStory);
    }

    @Test
    public void findAll_mockStoryActive_returnsMockStory() {
        // Mocking that findAllActive returns a list containing mockStory
        Mockito.when(storyRepository.findAll()).thenReturn(Collections.singletonList(mockStory));

        // Mocking that mockStory is active
        Mockito.when(mockStory.isActive()).thenReturn(true);

        final List<Story> stories = storyService.findAllActive();

        // Verifying that findAll was called
        Mockito.verify(storyRepository).findAll();

        // Verifying that mockStory was returned
        Assert.assertEquals(Collections.singletonList(mockStory), stories);
    }

    @Test
    public void findAll_mockStoryDeleted_returnsEmptyList() {
        // Mocking that findAllActive returns a list containing mockStory
        Mockito.when(storyRepository.findAll()).thenReturn(Collections.singletonList(mockStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        final List<Story> stories = storyService.findAllActive();

        // Verifying that findAll was called
        Mockito.verify(storyRepository).findAll();

        // Verifying that no story was returned
        Assert.assertEquals(Collections.emptyList(), stories);
    }

    @Test
    public void findActiveByCreationDate_mockStoryActive_returnsMockStory() {
        // Mocking that findByCreationDateAfter returns a list containing mockStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Collections.singletonList(mockStory));

        // Mocking that mockStory is active
        Mockito.when(mockStory.isActive()).thenReturn(true);

        final List<Story> stories = storyService.findActiveByCreationDateDuration(DURATION);

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that mockStory was returned
        Assert.assertEquals(Collections.singletonList(mockStory), stories);
    }

    @Test
    public void findActiveByCreationDate_mockStoryDeleted_returnsEmptyList() {
        // Mocking that findByCreationDateAfter returns a list containing mockStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Collections.singletonList(mockStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        final List<Story> stories = storyService.findActiveByCreationDateDuration(DURATION);

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that no story was returned
        Assert.assertEquals(Collections.emptyList(), stories);
    }


    @Test
    public void findActiveByDuration_mockStoryActive_returnsMockStory() {
        // Mocking that findByLastUpdatedAfter returns a list containing mockStory
        Mockito.when(storyRepository.findByLastUpdatedAfter(Matchers.anyLong())).thenReturn(Collections.singletonList(mockStory));

        // Mocking that mockStory is active
        Mockito.when(mockStory.isActive()).thenReturn(true);

        final List<Story> stories = storyService.findActiveByLastUpdatedDuration(DURATION);

        // Verifying that findByLastUpdatedAfter was called
        Mockito.verify(storyRepository).findByLastUpdatedAfter(Matchers.anyLong());

        // Verifying that mockStory was returned
        Assert.assertEquals(Collections.singletonList(mockStory), stories);
    }

    @Test
    public void findActiveByDuration_mockStoryDeleted_returnsEmptyList() {
        // Mocking that findByLastUpdatedAfter returns a list containing mockStory
        Mockito.when(storyRepository.findByLastUpdatedAfter(Matchers.anyLong())).thenReturn(Collections.singletonList(mockStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        final List<Story> stories = storyService.findActiveByLastUpdatedDuration(DURATION);

        // Verifying that findByLastUpdatedAfter was called
        Mockito.verify(storyRepository).findByLastUpdatedAfter(Matchers.anyLong());

        // Verifying that no story was returned
        Assert.assertEquals(Collections.emptyList(), stories);
    }

    @Test
    public void findTopStoryByTitleContaining_mockStoryDeleted_mockTopStoryActive_returnsMockTopStory() {
        // Mocking that findByTitleContaining returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByTitleContaining(TOPIC)).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        // Mocking that mockTopStory is active
        Mockito.when(mockTopStory.isActive()).thenReturn(true);

        final Optional<Story> topStory = storyService.findTopStoryByTitleContaining(TOPIC);

        // Verifying that findByTitleContaining was called
        Mockito.verify(storyRepository).findByTitleContaining(TOPIC);

        // Verifying that mockTopStory was returned
        Assert.assertEquals(Optional.of(mockTopStory), topStory);
    }

    @Test
    public void findTopStoryByTitleContaining_mockStoryActive_mockTopStoryActive_mockTopStoryBetterScore_returnsMockTopStory() {
        // Mocking that findByTitleContaining returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByTitleContaining(TOPIC)).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is active
        Mockito.when(mockStory.isActive()).thenReturn(true);

        // Mocking that mockTopStory is active
        Mockito.when(mockTopStory.isActive()).thenReturn(true);

        // Mocking that mockStory has a lower score, i.e. LOW_SCORE
        Mockito.when(mockStory.getScore()).thenReturn(LOW_SCORE);

        // Mocking that mockTopStory has a higher score, i.e. TOP_SCORE
        Mockito.when(mockTopStory.getScore()).thenReturn(TOP_SCORE);

        final Optional<Story> topStory = storyService.findTopStoryByTitleContaining(TOPIC);

        // Verifying that findByTitleContaining was called
        Mockito.verify(storyRepository).findByTitleContaining(TOPIC);

        // Verifying that mockTopStory was returned
        Assert.assertEquals(Optional.of(mockTopStory), topStory);
    }

    @Test
    public void findTopStoryByTitleContaining_mockStoryDeleted_mockTopStoryDeleted_returnsEmptyList() {
        // Mocking that findByTitleContaining returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByTitleContaining(TOPIC)).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        // Mocking that mockTopStory is deleted
        Mockito.when(mockTopStory.isActive()).thenReturn(false);

        final Optional<Story> topStory = storyService.findTopStoryByTitleContaining(TOPIC);

        // Verifying that findByTitleContaining was called
        Mockito.verify(storyRepository).findByTitleContaining(TOPIC);

        // Verifying that no story was returned
        Assert.assertEquals(Optional.empty(), topStory);
    }

    @Test
    public void findTopStoryByTitleContainingAndCreationDate_mockStoryDeleted_mockTopStoryActive_returnsMockTopStory() {
        // Mocking that findByCreationDateAfter returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        // Mocking that mockTopStory is active
        Mockito.when(mockTopStory.isActive()).thenReturn(true);

        // Mocking that mockTopStory contains TOPIC
        Mockito.when(mockTopStory.getTitle()).thenReturn(TITLE + TOPIC);

        final Optional<Story> topStory = storyService.findTopStoryByTitleContainingAndCreationDate(TOPIC, DurationType.DAILY.getDuration());

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that mockTopStory was returned
        Assert.assertEquals(Optional.of(mockTopStory), topStory);
    }

    @Test
    public void findTopStoryByTitleContainingAndCreationDate_mockStoryActive_mockTopStoryActive_mockTopStoryBetterScore_returnsMockTopStory() {
        // Mocking that findByCreationDateAfter returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is active
        Mockito.when(mockStory.isActive()).thenReturn(true);

        // Mocking that mockTopStory is active
        Mockito.when(mockTopStory.isActive()).thenReturn(true);

        // Mocking that mockStory has a lower score, i.e. LOW_SCORE
        Mockito.when(mockStory.getScore()).thenReturn(LOW_SCORE);

        // Mocking that mockTopStory has a higher score, i.e. TOP_SCORE
        Mockito.when(mockTopStory.getScore()).thenReturn(TOP_SCORE);

        // Mocking that mockStory contains TOPIC
        Mockito.when(mockStory.getTitle()).thenReturn(TITLE + TOPIC);

        // Mocking that mockTopStory contains TOPIC
        Mockito.when(mockTopStory.getTitle()).thenReturn(TITLE + TOPIC);

        final Optional<Story> topStory = storyService.findTopStoryByTitleContainingAndCreationDate(TOPIC, DurationType.DAILY.getDuration());

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that mockTopStory was returned
        Assert.assertEquals(Optional.of(mockTopStory), topStory);
    }

    @Test
    public void findTopStoryByTitleContainingAndCreationDate_mockStoryDeleted_mockTopStoryDeleted_returnsEmptyOptional() {
        // Mocking that findByCreationDateAfter returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        // Mocking that mockTopStory is deleted
        Mockito.when(mockTopStory.isActive()).thenReturn(false);

        final Optional<Story> topStory = storyService.findTopStoryByTitleContainingAndCreationDate(TOPIC, DurationType.DAILY.getDuration());

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that no story was returned
        Assert.assertEquals(Optional.empty(), topStory);
    }

    @Test
    public void findOverallTopStoriesByCreationDate_returnsTopStories() {
        // Mocking that findByTitleContaining returns a list containing mockStory, mockTopStory and mockAverageStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory, mockAverageStory));

        // Mocking that mockStory is NOT deleted
        Mockito.when(mockStory.isActive()).thenReturn(true);

        // Mocking that mockAverageStory is NOT deleted
        Mockito.when(mockAverageStory.isActive()).thenReturn(true);

        // Mocking that mockTopStory is NOT deleted
        Mockito.when(mockTopStory.isActive()).thenReturn(true);

        // Mocking that mockStory has a lower score, i.e. LOW_SCORE
        Mockito.when(mockStory.getScore()).thenReturn(LOW_SCORE);

        // Mocking that mockAverageStory has an average score, i.e. AVERAGE_SCORE
        Mockito.when(mockAverageStory.getScore()).thenReturn(AVERAGE_SCORE);

        // Mocking that mockTopStory has a higher score, i.e. TOP_SCORE
        Mockito.when(mockTopStory.getScore()).thenReturn(TOP_SCORE);

        final int N = 2;

        final List<Story> topStories = storyService.findOverallTopStoriesByCreationDate(DurationType.WEEKLY.getDuration(), N);

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that N top stories were returned
        Assert.assertEquals(N, topStories.size());

        // Verifying that mockTopStory and mockAverageStory were the returned top stories
        Assert.assertEquals(Arrays.asList(mockTopStory, mockAverageStory), topStories);
    }

    @Test
    public void findOverallTopStoriesByCreationDate_mockStoryDeleted_mockTopStoryDeleted_returnsEmptyList() {

        // Mocking that findByTitleContaining returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory));

        // Mocking that mockStory is deleted
        Mockito.when(mockStory.isActive()).thenReturn(false);

        // Mocking that mockTopStory is deleted
        Mockito.when(mockTopStory.isActive()).thenReturn(false);

        final int N = 2;

        final List<Story> topStories = storyService.findOverallTopStoriesByCreationDate(DurationType.WEEKLY.getDuration(), N);

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that no story was returned
        Assert.assertEquals(topStories, Collections.emptyList());
    }

    @Test
    public void findTopStoriesByTitleContainingAndCreationDate_returnsTopStories() {
        // Mocking that findByTitleContaining returns a list containing mockStory, mockTopStory and mockAverageStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory, mockAverageStory));

        // Mocking that mockStory is NOT deleted
        Mockito.when(mockStory.isActive()).thenReturn(true);

        // Mocking that mockAverageStory is NOT deleted
        Mockito.when(mockAverageStory.isActive()).thenReturn(true);

        // Mocking that mockTopStory is NOT deleted
        Mockito.when(mockTopStory.isActive()).thenReturn(true);

        // Mocking that mockStory has a lower score, i.e. LOW_SCORE
        Mockito.when(mockStory.getScore()).thenReturn(LOW_SCORE);

        // Mocking that mockAverageStory has an average score, i.e. AVERAGE_SCORE
        Mockito.when(mockAverageStory.getScore()).thenReturn(AVERAGE_SCORE);

        // Mocking that mockTopStory has a higher score, i.e. TOP_SCORE
        Mockito.when(mockTopStory.getScore()).thenReturn(TOP_SCORE);

        // Mocking that mockStory contains TOPIC
        Mockito.when(mockStory.getTitle()).thenReturn(TITLE + TOPIC);

        // Mocking that mockAverageStory does NOT contain TOPIC
        Mockito.when(mockAverageStory.getTitle()).thenReturn(TITLE);

        // Mocking that mockTopStory contains TOPIC
        Mockito.when(mockTopStory.getTitle()).thenReturn(TITLE + TOPIC);

        final int N = 2;

        final List<Story> topStories = storyService.findTopStoriesByTitleContainingAndCreationDate(TOPIC, DurationType.WEEKLY.getDuration(), N);

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that N top stories were returned
        Assert.assertEquals(N, topStories.size());

        // Verifying that mockTopStory and mockStory were the returned top stories
        Assert.assertEquals(Arrays.asList(mockTopStory, mockStory), topStories);
    }

    @Test
    public void findTopStoriesByTitleContainingAndCreationDate_mockStoryDeleted_mockTopStoryDeleted_returnsEmptyList() {

        // Mocking that findByTitleContaining returns a list containing mockStory and mockTopStory
        Mockito.when(storyRepository.findByCreationDateAfter(Matchers.anyLong())).thenReturn(Arrays.asList(mockStory, mockTopStory, mockAverageStory));

        // Mocking that mockStory is NOT deleted
        Mockito.when(mockStory.isActive()).thenReturn(true);

        // Mocking that mockAverageStory is NOT deleted
        Mockito.when(mockAverageStory.isActive()).thenReturn(true);

        // Mocking that mockTopStory is deleted
        Mockito.when(mockTopStory.isActive()).thenReturn(false);

        // Mocking that mockStory has a lower score, i.e. LOW_SCORE
        Mockito.when(mockStory.getScore()).thenReturn(LOW_SCORE);

        // Mocking that mockAverageStory has an average score, i.e. AVERAGE_SCORE
        Mockito.when(mockAverageStory.getScore()).thenReturn(AVERAGE_SCORE);

        // Mocking that mockTopStory has a higher score, i.e. TOP_SCORE
        Mockito.when(mockTopStory.getScore()).thenReturn(TOP_SCORE);

        // Mocking that mockStory contains TOPIC
        Mockito.when(mockStory.getTitle()).thenReturn(TITLE + TOPIC);

        // Mocking that mockAverageStory does NOT contain TOPIC
        Mockito.when(mockAverageStory.getTitle()).thenReturn(TITLE);

        // Mocking that mockTopStory contains TOPIC
        Mockito.when(mockTopStory.getTitle()).thenReturn(TITLE + TOPIC);

        final int N = 2;

        final List<Story> topStories = storyService.findTopStoriesByTitleContainingAndCreationDate(TOPIC, DurationType.WEEKLY.getDuration(), N);

        // Verifying that findByCreationDateAfter was called
        Mockito.verify(storyRepository).findByCreationDateAfter(Matchers.anyLong());

        // Verifying that mockStory were the returned top stories
        Assert.assertEquals(Collections.singletonList(mockStory), topStories);
    }

    @Test
    public void deleteUnusedStoriesByCreationDate_unusedDigestExists_unusedDigestDeleted() {

        // Mocking that mockStory contains STORY_ID
        Mockito.when(mockStory.getId()).thenReturn(STORY_ID);

        // Mocking all unused in database, i.e. mockStory
        Mockito.when(storyRepository.findUnusedByCreationDate(Matchers.anyLong())).thenReturn(Collections.singletonList(mockStory));

        storyService.deleteUnusedStoriesByCreationDate(DurationType.WEEKLY.getDuration());

        // Verifying that mockStory was deleted
        Mockito.verify(storyRepository).delete(mockStory.getId());
    }

    @Test
    public void deleteUnusedStoriesByCreationDate_unusedDigestDoesNotExist_doesNothing() {

        // Mocking that mockStory contains STORY_ID
        Mockito.when(mockStory.getId()).thenReturn(STORY_ID);

        // Mocking that there are no unused stories in database
        Mockito.when(storyRepository.findUnusedByCreationDate(Matchers.anyLong())).thenReturn(Collections.emptyList());

        storyService.deleteUnusedStoriesByCreationDate(DurationType.WEEKLY.getDuration());

        // Verifying that mockStory was NOT deleted
        Mockito.verify(storyRepository, Mockito.never()).delete(mockStory.getId());
    }

}
