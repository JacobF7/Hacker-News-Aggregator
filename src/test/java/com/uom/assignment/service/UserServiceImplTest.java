package com.uom.assignment.service;

import com.uom.assignment.controller.BusinessErrorException;
import com.uom.assignment.dao.*;
import com.uom.assignment.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;


/**
 * A test suite for {@link UserService}.
 *
 * Created by jacobfalzon on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private Digest mockDigest;

    @Mock
    private Topic mockTopic;

    @Mock
    private User mockUser;

    @Mock
    private UserTopic mockUserTopic;

    @Mock
    private Story mockStory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TopicService topicService;

    @Mock
    private UserTopicService userTopicService;

    @Mock
    private DigestService digestService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long USER_ID = new SecureRandom().nextLong();
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TOPIC = "topic";

    @Test
    public void findAll_returnsUser() {

        // Mocking mockUser exists
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(mockUser));

        // Verifying that mockUser is returned
        Assert.assertEquals(Collections.singletonList(mockUser), userService.findAll());
    }

    @Test
    public void findByUsername_returnsUser() {

        // Mocking mockUser to contain USERNAME
        Mockito.when(mockUser.getId()).thenReturn(USER_ID);
        Mockito.when(mockUser.getUsername()).thenReturn(USERNAME);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockUser));

        Assert.assertEquals(Optional.of(mockUser), userService.findByUsername(USERNAME));
    }

    @Test(expected = BusinessErrorException.class)
    public void create_usernameExists_throwsBusinessErrorException() {

        // Mocking mockUser to contain USERNAME
        Mockito.when(mockUser.getUsername()).thenReturn(USERNAME);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(mockUser));

        userService.create(USERNAME, PASSWORD, Collections.singleton(TOPIC));
    }

    @Test
    public void create_createsNewUser() {

        final Set<String> topics = Collections.singleton(TOPIC);

        // Mocking that no other User exists with this USERNAME
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        // Mocking that TOPIC was created/fetched
        Mockito.when(topicService.create(TOPIC)).thenReturn(mockTopic);

        userService.create(USERNAME, PASSWORD, topics);

        // Verifying that the PASSWORD was encoded before the User is saved
        Mockito.verify(passwordEncoder).encode(PASSWORD);

        // Verifying that a new User is created
        Mockito.verify(userRepository).save(Matchers.any(User.class));

        // Verifying that the new User was subscribed to the set of desired topics
        Mockito.verify(userTopicService).subscribe(Matchers.any(User.class), Matchers.eq(Collections.singleton(mockTopic)));
    }

    @Test(expected = BusinessErrorException.class)
    public void update_userIdDoesNotExist_throwsException() {

        // Mocking that no User exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(null);

        userService.update(USER_ID, Collections.singleton(TOPIC));
    }

    @Test
    public void update_userIdExists_subscribesToTopics() {

        // Mocking that mockUser exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(mockUser);

        // Mocking that TOPIC was created/fetched
        Mockito.when(topicService.create(TOPIC)).thenReturn(mockTopic);

        userService.update(USER_ID, Collections.singleton(TOPIC));

        // Verifying that mockUser was subscribed to the set of desired topics
        Mockito.verify(userTopicService).subscribe(mockUser, Collections.singleton(mockTopic));
    }

    @Test(expected = BusinessErrorException.class)
    public void getTopStories_userIdDoesNotExist_throwsException() {

        // Mocking that no User exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(null);

        userService.getTopStories(USER_ID);
    }

    @Test
    public void getTopStories_userIdExists_returnsTopicTopStory() {

        // Mocking that mockUser exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(mockUser);

        // Mocking that mockUser is subscribed to mockUserTopic
        Mockito.when(mockUser.getUserTopics()).thenReturn(Collections.singleton(mockUserTopic));

        // Mocking that mockUserTopic is effective
        Mockito.when(mockUserTopic.isEffective()).thenReturn(true);

        // Mocking that mockUserTopic contains mockUser
        Mockito.when(mockUserTopic.getUser()).thenReturn(mockUser);

        // Mocking that mockUserTopic contains mockTopic
        Mockito.when(mockUserTopic.getTopic()).thenReturn(mockTopic);

        // Mocking that mockTopic has name TOPIC
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC);

        // Mocking that the top story for mockTopic is mockStory
        Mockito.when(mockTopic.getTopStory()).thenReturn(mockStory);

        final Map<Topic, Story> topStories = userService.getTopStories(USER_ID);

        // Verifying that the top story for mockTopic is mockStory
        Assert.assertEquals(Collections.singletonMap(mockTopic, mockStory), topStories);
    }

    @Test
    public void getTopStories_userIdExists_noTopStoryExists_returnsNullTopStory() {

        // Mocking that mockUser exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(mockUser);

        // Mocking that mockUser is subscribed to mockUserTopic
        Mockito.when(mockUser.getUserTopics()).thenReturn(Collections.singleton(mockUserTopic));

        // Mocking that mockUserTopic is effective
        Mockito.when(mockUserTopic.isEffective()).thenReturn(true);

        // Mocking that mockUserTopic contains mockUser
        Mockito.when(mockUserTopic.getUser()).thenReturn(mockUser);

        // Mocking that mockUserTopic contains mockTopic
        Mockito.when(mockUserTopic.getTopic()).thenReturn(mockTopic);

        // Mocking that mockTopic has name TOPIC
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC);

        // Mocking that the top story for mockTopic is null
        Mockito.when(mockTopic.getTopStory()).thenReturn(null);

        final Map<Topic, Story> topStories = userService.getTopStories(USER_ID);

        // Verifying that the top story for mockTopic is null
        Assert.assertEquals(Collections.singletonMap(mockTopic, null), topStories);
    }

    @Test
    public void getTopStories_userIdExists_userTopicIneffective_returnsEmptyMap() {

        // Mocking that mockUser exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(mockUser);

        // Mocking that mockUser is subscribed to mockUserTopic
        Mockito.when(mockUser.getUserTopics()).thenReturn(Collections.singleton(mockUserTopic));

        // Mocking that mockUserTopic is ineffective
        Mockito.when(mockUserTopic.isEffective()).thenReturn(false);

        final Map<Topic, Story> topStories = userService.getTopStories(USER_ID);

        // Verifying that an empty map is returned
        Assert.assertEquals(Collections.emptyMap(), topStories);
    }

    @Test(expected = BusinessErrorException.class)
    public void getDigests_userIdDoesNotExist_throwsException() {

        // Mocking that no User exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(null);

        final LocalDate endDate = LocalDate.now(); // now
        final LocalDate startDate = endDate.minusWeeks(1L); // last week

        userService.getDigests(USER_ID, startDate, endDate);
    }

    @Test
    public void getDigests_userIdExists_returnsDigests() {

        // Mocking that mockUser exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(mockUser);

        final LocalDate endDate = LocalDate.now(); // now
        final LocalDate startDate = endDate.minusWeeks(1L); // last week

        // Mocking that mockDigest is returned grouped by startDate
        Mockito.when(digestService.findDigests(mockUser, startDate, endDate)).thenReturn(Collections.singletonMap(startDate, Collections.singletonList(mockDigest)));

        final Map<LocalDate, List<Digest>> digests = userService.getDigests(USER_ID, startDate, endDate);

        // Verifying that an attempt was made to get the digests
        Mockito.verify(digestService).findDigests(mockUser, startDate, endDate);

        // Verifying that mockDigest is returned
        Assert.assertEquals(Collections.singletonMap(startDate, Collections.singletonList(mockDigest)), digests);
    }

    @Test(expected = BusinessErrorException.class)
    public void getLatestDigests_userIdDoesNotExist_throwsException() {

        // Mocking that no User exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(null);

        userService.getLatestDigests(USER_ID);
    }

    @Test
    public void getLatestDigests_userIdExists_returnsDigests() {

        // Mocking that mockUser exists with USER_ID
        Mockito.when(userRepository.findOne(USER_ID)).thenReturn(mockUser);

        final LocalDate latestDate = LocalDate.now().minusWeeks(1L); // 1 week ago

        // Mocking that mockDigest is returned for latestDate
        Mockito.when(digestService.findLatestDigests(mockUser)).thenReturn(Collections.singletonMap(latestDate, Collections.singletonList(mockDigest)));

        final Map<LocalDate, List<Digest>> digests = userService.getLatestDigests(USER_ID);

        // Verifying that an attempt was made to get the latest digests
        Mockito.verify(digestService).findLatestDigests(mockUser);

        // Verifying that mockDigest is returned
        Assert.assertEquals(Collections.singletonMap(latestDate, Collections.singletonList(mockDigest)), digests);
    }
}
