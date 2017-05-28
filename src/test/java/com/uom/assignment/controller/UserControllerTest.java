package com.uom.assignment.controller;

import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.dao.Session;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.model.request.TopicModel;
import com.uom.assignment.model.request.UserModel;
import com.uom.assignment.model.response.TopStoriesModel;
import com.uom.assignment.model.response.UserTopicModel;
import com.uom.assignment.service.SessionService;
import com.uom.assignment.service.TopicService;
import com.uom.assignment.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A test for {@link UserController}.
 *
 * Created by jacobfalzon on 07/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private Story mockStory;

    @Mock
    private Topic mockTopic;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private static final Random RANDOM = new Random();
    private static final Long USER_ID = RANDOM.nextLong();
    private static final String TOPIC = "Topic";
    private static final Set<String> TOPICS = Collections.singleton(TOPIC);
    private static final String TITLE = "title";
    private static final String URL = "url";
    private static final Long SCORE = RANDOM.nextLong();

    @Before
    public void setup() {

        // Mocking that mockTopic has name TOPIC
        Mockito.when(mockTopic.getName()).thenReturn(TOPIC);

        // Mocking that mockStory has TITLE
        Mockito.when(mockStory.getTitle()).thenReturn(TITLE);

        // Mocking that mockStory has URL
        Mockito.when(mockStory.getUrl()).thenReturn(URL);

        // Mocking that mockStory has SCORE
        Mockito.when(mockStory.getScore()).thenReturn(SCORE);
    }


    @Test
    public void create_delegateToUserService_returnsUserTopicModel() {

        final String username = "username";
        final String password = "password";
        final UserModel userModel = new UserModel(username, password, TOPICS);

        // Mocking that the create was successful and USER_ID was returned
        Mockito.when(userService.create(userModel.getUsername(), userModel.getPassword(), userModel.getTopics())).thenReturn(USER_ID);

        final ResponseEntity<UserTopicModel> response = userController.create(userModel);

        // Verifying that user creation was attempted
        Mockito.verify(userService).create(userModel.getUsername(), userModel.getPassword(), userModel.getTopics());

        // Verifying that userId was returned
        Assert.assertEquals(response.getBody().getUserId(), USER_ID);

        // Verifying that topics were returned
        Assert.assertEquals(response.getBody().getTopics(), sanitize(TOPICS));
    }

    @Test(expected = BusinessErrorException.class)
    public void update_userIdDoesNotMatchToken_throwsException() {
        final TopicModel topicModel = new TopicModel(TOPICS);

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        final Long differentUserId = USER_ID + 1;

        userController.update(mockRequest, differentUserId, topicModel);
    }

    @Test
    public void update_userIdMatchesToken_delegateToUserService_returnsUserTopicModel() {
        final TopicModel topicModel = new TopicModel(TOPICS);

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        final ResponseEntity<UserTopicModel> response = userController.update(mockRequest, USER_ID, topicModel);

        // Verifying that user update was attempted
        Mockito.verify(userService).update(USER_ID, topicModel.getTopics());

        // Verifying that userId was returned
        Assert.assertEquals(response.getBody().getUserId(), USER_ID);

        // Verifying that topics were returned
        Assert.assertEquals(sanitize(TOPICS), response.getBody().getTopics());
    }

    @Test(expected = BusinessErrorException.class)
    public void getRealTimeStories_userIdDoesNotMatchToken_throwsException() {

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        final Long differentUserId = USER_ID + 1;

        userController.getRealTimeTopStories(mockRequest, differentUserId);
    }

    @Test
    public void getRealTimeStories_userIdMatchesToken_delegateToUserService_topStoryFound_returnsTopStoriesModel() {

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        // Mocking that mockStory is the top story for mockTopic
        Mockito.when(userService.getRealTimeTopStories(USER_ID)).thenReturn(Collections.singletonMap(mockTopic, mockStory));

        final ResponseEntity<TopStoriesModel> response = userController.getRealTimeTopStories(mockRequest, USER_ID);

        // Verifying that getRealTimeTopStories was attempted
        Mockito.verify(userService).getRealTimeTopStories(USER_ID);

        // Verifying that mockStory is the top story for mockTopic
        Assert.assertEquals(new TopStoriesModel(Collections.singletonMap(mockTopic, mockStory)), response.getBody());
    }

    @Test
    public void getRealTimeStories_userIdMatchesToken_delegateToUserService_topStoryNotFound_returnsTopStoriesModel() {

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        // Mocking that null is the top story for mockTopic
        Mockito.when(userService.getRealTimeTopStories(USER_ID)).thenReturn(Collections.singletonMap(mockTopic, null));

        final ResponseEntity<TopStoriesModel> response = userController.getRealTimeTopStories(mockRequest, USER_ID);

        // Verifying that getRealTimeTopStories was attempted
        Mockito.verify(userService).getRealTimeTopStories(USER_ID);

        // Verifying that mockTopic has no top story
        Assert.assertEquals(new TopStoriesModel(Collections.singletonMap(mockTopic, null)), response.getBody());
    }

    @Test
    public void getRealTimeStories_userIdMatchesToken_delegateToUserService_noUserTopic_returnsTopStoriesModel() {

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        // Mocking that no top stories are returned
        Mockito.when(userService.getRealTimeTopStories(USER_ID)).thenReturn(Collections.emptyMap());

        final ResponseEntity<TopStoriesModel> response = userController.getRealTimeTopStories(mockRequest, USER_ID);

        // Verifying that getRealTimeTopStories was attempted
        Mockito.verify(userService).getRealTimeTopStories(USER_ID);

        // Verifying that no top stories are returned
        Assert.assertEquals(new TopStoriesModel(Collections.emptyMap()), response.getBody());
    }

    @Test(expected = BusinessErrorException.class)
    public void getTopStories_userIdDoesNotMatchToken_throwsException() {

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        final Long differentUserId = USER_ID + 1;

        userController.getTopStories(mockRequest, differentUserId);
    }

    @Test
    public void getTopStories_userIdMatchesToken_delegateToUserService_topStoryFound_returnsTopStoriesModel() {

        // Mocking that the request contains USER_ID
        Mockito.when(mockRequest.getAttribute(AuthorizationHeader.USER_ID)).thenReturn(USER_ID);

        // Mocking that mockStory is the top story for mockTopic
        Mockito.when(userService.getTopStories(USER_ID)).thenReturn(Collections.singletonMap(mockTopic, mockStory));

        final ResponseEntity<TopStoriesModel> response = userController.getTopStories(mockRequest, USER_ID);

        // Verifying that getTopStories was attempted
        Mockito.verify(userService).getTopStories(USER_ID);

        // Verifying that mockStory is the top story for mockTopic
        Assert.assertEquals(new TopStoriesModel(Collections.singletonMap(mockTopic, mockStory)), response.getBody());
    }

    private Set<String> sanitize(final Set<String> topics) {
        return topics.stream().map(TopicService::sanitize).collect(Collectors.toSet());
    }
}
