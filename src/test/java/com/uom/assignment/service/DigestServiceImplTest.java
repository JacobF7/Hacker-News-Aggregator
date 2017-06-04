package com.uom.assignment.service;

import com.google.common.collect.Sets;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.repository.DigestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    private User mockUser;

    @Mock
    private Digest mockDigest;

    @Mock
    private Digest mockFirstWeekFirstDigest;

    @Mock
    private Digest mockFirstWeekSecondDigest;

    @Mock
    private Digest mockSecondWeekFirstDigest;

    @Mock
    private DigestRepository digestRepository;

    @InjectMocks
    private DigestServiceImpl digestService;

    private static final String TOPIC_NAME = "topic";
    private static final Long DIGEST_ID = new Random().nextLong();

    private static final LocalDate TWO_WEEKS_AGO = LocalDate.now().minusWeeks(2L); // two weeks ago
    private static final LocalDate ONE_WEEK_AGO = LocalDate.now().minusWeeks(1L); // one week ago

    @Test
    public void createTopicDigest_persistsDigest() {

        Mockito.when(mockTopic.getName()).thenReturn(TOPIC_NAME);

        digestService.createTopicDigest(mockTopic, mockStory, Collections.singleton(mockUser), DurationType.WEEKLY);

        final ArgumentCaptor<Digest> argumentCaptor = ArgumentCaptor.forClass(Digest.class);

        // Verifying that a digest was created
        Mockito.verify(digestRepository).save(argumentCaptor.capture());

        final Digest digest = argumentCaptor.getValue();

        // Verifying that the digest contains mockTopic
        Assert.assertEquals(mockTopic, digest.getTopic());

        // Verifying that the digest contains mockStory
        Assert.assertEquals(mockStory, digest.getStory());

        // Verifying that the digest is associated to mockUser
        Assert.assertEquals(Collections.singleton(mockUser), digest.getUsers());

        // Verifying that the digest is NOT an overall digest
        Assert.assertFalse(digest.getOverall());
    }

    @Test
    public void createOverallDigest_persistsDigest() {

        digestService.createOverallDigest(mockStory, Collections.singleton(mockUser), DurationType.WEEKLY);

        final ArgumentCaptor<Digest> argumentCaptor = ArgumentCaptor.forClass(Digest.class);

        // Verifying that a digest was created
        Mockito.verify(digestRepository).save(argumentCaptor.capture());

        final Digest digest = argumentCaptor.getValue();

        // Verifying that digest does NOT contain a Topic and mockStory and that is not an overall digest
        Assert.assertNull(digest.getTopic());

        // Verifying that digest contains mockStory
        Assert.assertEquals(mockStory, digest.getStory());

        // Verifying that the digest is associated to mockUser
        Assert.assertEquals(Collections.singleton(mockUser), digest.getUsers());

        // Verifying that the digest is an overall digest
        Assert.assertTrue(digest.getOverall());
    }

    @Test
    public void getDigests_returnsDigests() {

        final LocalDate endDate = LocalDate.now(); // now
        final LocalDate startDate = endDate.minusWeeks(2L); // two weeks ago

        // Mocking that mockFirstWeekFirstDigest occurred TWO_WEEKS_AGO for mockUser
        Mockito.when(mockFirstWeekFirstDigest.getCreationDate()).thenReturn(DigestService.toEpoch(TWO_WEEKS_AGO));
        Mockito.when(mockFirstWeekFirstDigest.getOverall()).thenReturn(false);
        Mockito.when(mockFirstWeekFirstDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that mockFirstWeekSecondDigest occurred TWO_WEEKS_AGO for mockUser
        Mockito.when(mockFirstWeekSecondDigest.getCreationDate()).thenReturn(DigestService.toEpoch(TWO_WEEKS_AGO));
        Mockito.when(mockFirstWeekSecondDigest.getOverall()).thenReturn(false);
        Mockito.when(mockFirstWeekSecondDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that mockSecondWeekFirstDigest occurred ONE_WEEK_AGO and is an overall digest for mockUser
        Mockito.when(mockSecondWeekFirstDigest.getCreationDate()).thenReturn(DigestService.toEpoch(ONE_WEEK_AGO));
        Mockito.when(mockSecondWeekFirstDigest.getOverall()).thenReturn(true);
        Mockito.when(mockSecondWeekFirstDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that mockFirstWeekFirstDigest, mockFirstWeekSecondDigest and mockSecondWeekDigest are returned
        Mockito.when(digestRepository.findByUsersAndCreationDateBetween(mockUser, DigestService.toEpoch(startDate), DigestService.toEpoch(endDate))).thenReturn(Sets.newHashSet(mockFirstWeekFirstDigest, mockFirstWeekSecondDigest, mockSecondWeekFirstDigest));

        final Map<LocalDate, List<Digest>> digests = digestService.findDigests(mockUser, startDate, endDate);

        // Verifying that an attempt was made to retrieve the digests was created
        Mockito.verify(digestRepository).findByUsersAndCreationDateBetween(mockUser, DigestService.toEpoch(startDate), DigestService.toEpoch(endDate));

        // Verifying that the digests were grouped by TWO_WEEKS_AGO and ONE_WEEK_AGO
        Assert.assertEquals(2, digests.size());

        // Verifying that the digest for TWO_WEEKS_AGO contains mockFirstWeekFirstDigest and mockFirstWeekSecondDigest
        Assert.assertTrue(digests.get(TWO_WEEKS_AGO).containsAll(Arrays.asList(mockFirstWeekSecondDigest, mockFirstWeekFirstDigest)));

        // Verifying that the digest for ONE_WEEK_AGO contains mockSecondWeekFirstDigest
        Assert.assertTrue(digests.get(ONE_WEEK_AGO).containsAll(Collections.singletonList(mockSecondWeekFirstDigest)));
    }

    @Test
    public void getLatestDigests_digestDoesNotExist_returnsEmptyMap() {

        // Mocking that the latest digest does not exist since the most recent digest creation date for mockUser cannot be retrieved
        Mockito.when(digestRepository.findMaxCreationDateByUser(mockUser)).thenReturn(null);

        final Map<LocalDate, List<Digest>> digests = digestService.findLatestDigests(mockUser);

        // Verifying that an attempt was made to retrieve the date of the most recent digest creation date for mockUser
        Mockito.verify(digestRepository).findMaxCreationDateByUser(mockUser);

        // Verifying that an attempt was NOT made to retrieve the digests by user and most recent digest creation date
        Mockito.verify(digestRepository, Mockito.never()).findByUsersAndCreationDate(Matchers.eq(mockUser), Matchers.any());

        // Verifying that an empty map was returned
        Assert.assertEquals(Collections.emptyMap(), digests);
    }

    @Test
    public void getLatestDigests_returnsLatestDigests() {

        // Mocking that mockFirstWeekSecondDigest occurred ONE_WEEK_AGO for mockUser
        Mockito.when(mockSecondWeekFirstDigest.getCreationDate()).thenReturn(DigestService.toEpoch(ONE_WEEK_AGO));
        Mockito.when(mockSecondWeekFirstDigest.getOverall()).thenReturn(true);
        Mockito.when(mockSecondWeekFirstDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that the latest digest for mockUser was created on ONE_WEEK_AGO
        Mockito.when(digestRepository.findMaxCreationDateByUser(mockUser)).thenReturn(DigestService.toEpoch(ONE_WEEK_AGO));

        // Mocking that the latest digest for mockUser is mockSecondWeekFirstDigest
        Mockito.when(digestRepository.findByUsersAndCreationDate(mockUser, DigestService.toEpoch(ONE_WEEK_AGO))).thenReturn(Collections.singleton(mockSecondWeekFirstDigest));

        final Map<LocalDate, List<Digest>> digests = digestService.findLatestDigests(mockUser);

        // Verifying that an attempt was made to retrieve the date of the most recent digest creation date for mockUser
        Mockito.verify(digestRepository).findMaxCreationDateByUser(mockUser);

        // Verifying that an attempt was made to retrieve the digests by user and most recent digest creation date, i.e. ONE_WEEK_AGO
        Mockito.verify(digestRepository).findByUsersAndCreationDate(mockUser, DigestService.toEpoch(ONE_WEEK_AGO));

        // Verifying that the digests were grouped by ONE_WEEK_AGO only
        Assert.assertEquals(1, digests.size());

        // Verifying that the digest for ONE_WEEK_AGO contains mockSecondWeekFirstDigest
        Assert.assertTrue(digests.get(ONE_WEEK_AGO).containsAll(Collections.singletonList(mockSecondWeekFirstDigest)));
    }

    @Test
    public void deleteExpiredDigests_expiredDigestExists_expiredDigestDeleted() {

        // Mocking that mockDigest contains DIGEST_ID
        Mockito.when(mockDigest.getId()).thenReturn(DIGEST_ID);

        // Mocking all digests stored in database, i.e. mockDigest
        Mockito.when(digestRepository.findAll()).thenReturn(Collections.singletonList(mockDigest));

        // Mock creationDate as 1 year ago, i.e. mockDigest is expired.
        Mockito.when(mockDigest.getCreationDate()).thenReturn(getExpiredCreationDate());

        digestService.deleteExpiredDigests();

        // Verifying that mockDigest was deleted
        Mockito.verify(digestRepository).delete(mockDigest.getId());
    }

    @Test
    public void deleteExpiredDigests_expiredDigestDoesNotExist_doesNothing() {

        // Mocking that mockDigest contains DIGEST_ID
        Mockito.when(mockDigest.getId()).thenReturn(DIGEST_ID);

        // Mocking all digests stored in database, i.e. mockDigest
        Mockito.when(digestRepository.findAll()).thenReturn(Collections.singletonList(mockDigest));

        // Mock lastActivity as now, i.e. mockSession is not expired.
        Mockito.when(mockDigest.getCreationDate()).thenReturn(System.currentTimeMillis());

        digestService.deleteExpiredDigests();

        // Verifying that mockDigest was NOT deleted
        Mockito.verify(digestRepository, Mockito.never()).delete(mockDigest.getId());
    }

    private long getExpiredCreationDate() {
        // Return the creationDate. This timestamp is expired by 1 year.
        return LocalDateTime.now().minusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
