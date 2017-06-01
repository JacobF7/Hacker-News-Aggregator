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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    private final LocalDate TWO_WEEKS_AGO = LocalDate.now().plusWeeks(-2L); // two weeks ago
    private final LocalDate ONE_WEEK_AGO = LocalDate.now().plusWeeks(-1L); // one week ago

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
        final LocalDate startDate = endDate.plusWeeks(-2L); // two weeks ago

        // Mocking that mockFirstWeekFirstDigest occurred TWO_WEEKS_AGO for mockUser
        Mockito.when(mockFirstWeekFirstDigest.getCreationDate()).thenReturn(toEpoch(TWO_WEEKS_AGO));
        Mockito.when(mockFirstWeekFirstDigest.getOverall()).thenReturn(false);
        Mockito.when(mockFirstWeekFirstDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that mockFirstWeekSecondDigest occurred TWO_WEEKS_AGO for mockUser
        Mockito.when(mockFirstWeekSecondDigest.getCreationDate()).thenReturn(toEpoch(TWO_WEEKS_AGO));
        Mockito.when(mockFirstWeekSecondDigest.getOverall()).thenReturn(false);
        Mockito.when(mockFirstWeekSecondDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that mockSecondWeekFirstDigest occurred ONE_WEEK_AGO and is an overall digest for mockUser
        Mockito.when(mockSecondWeekFirstDigest.getCreationDate()).thenReturn(toEpoch(ONE_WEEK_AGO));
        Mockito.when(mockSecondWeekFirstDigest.getOverall()).thenReturn(true);
        Mockito.when(mockSecondWeekFirstDigest.getUsers()).thenReturn(Collections.singleton(mockUser));

        // Mocking that mockFirstWeekFirstDigest, mockFirstWeekSecondDigest and mockSecondWeekDigest are returned
        Mockito.when(digestRepository.findByUsersAndCreationDateBetween(mockUser, toEpoch(startDate), toEpoch(endDate))).thenReturn(Sets.newHashSet(mockFirstWeekFirstDigest, mockFirstWeekSecondDigest, mockSecondWeekFirstDigest));

        final Map<LocalDate, List<Digest>> digests = digestService.findDigests(mockUser, startDate, endDate);

        // Verifying that an attempt was made to retrieve the digests was created
        Mockito.verify(digestRepository).findByUsersAndCreationDateBetween(mockUser, toEpoch(startDate), toEpoch(endDate));

        // Verifying that the digests were grouped by TWO_WEEKS_AGO and ONE_WEEK_AGO
        Assert.assertEquals(2, digests.size());

        // Verifying that the digest for TWO_WEEKS_AGO contains mockFirstWeekFirstDigest and mockFirstWeekSecondDigest
        Assert.assertTrue(digests.get(TWO_WEEKS_AGO).containsAll(Arrays.asList(mockFirstWeekSecondDigest, mockFirstWeekFirstDigest)));

        // Verifying that the digest for ONE_WEEK_AGO contains mockSecondWeekFirstDigest
        Assert.assertTrue(digests.get(ONE_WEEK_AGO).containsAll(Collections.singletonList(mockSecondWeekFirstDigest)));
    }

    private Long toEpoch(final LocalDate date) {
        return date.atStartOfDay().atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
