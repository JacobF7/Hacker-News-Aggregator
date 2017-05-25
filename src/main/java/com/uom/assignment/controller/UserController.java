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
import com.uom.assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.Objects;

/**
 * Defines CRUD operations for a {@link User}.
 *
 * Created by jacobfalzon on 14/04/2017.
 */
@RestController
@RequestMapping(UserController.RESOURCE)
public class UserController {

    static final String RESOURCE = "/users";

    private UserService userService;
    private SessionService sessionService;

    @Autowired
    public UserController(final UserService userService, final SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserTopicModel> create(@RequestBody @Valid final UserModel user) {
        final Long userId = userService.create(user.getUsername(), user.getPassword(), user.getTopics());
        return ResponseEntity.ok(new UserTopicModel(userId, user.getTopics()));
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<UserTopicModel> update(@AuthorizationHeader @RequestHeader(AuthorizationHeader.AUTHORIZATION_HEADER) final String authorization,
                                                 @PathVariable final Long id,
                                                 @RequestBody @Valid final TopicModel topicModel) {

        // TODO CONFIRM THIS
        // Should NOT occur, due to Authentication Aspect
        final Session session = sessionService.findByToken(authorization).orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_TOKEN));

        // Make sure that the userId matches the id of the user obtained from the session
        if(!Objects.equals(id, session.getUser().getId())) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        userService.update(id, topicModel.getTopics());
        return ResponseEntity.ok(new UserTopicModel(id, topicModel.getTopics()));
    }

    @RequestMapping(value ="/{id}/real-time-stories", method = RequestMethod.GET)
    public ResponseEntity<TopStoriesModel> getRealTimeTopStories(@AuthorizationHeader @RequestHeader(AuthorizationHeader.AUTHORIZATION_HEADER) final String authorization,
                                                                 @PathVariable final Long id) {

        // Should NOT occur, due to Authentication Aspect
        final Session session = sessionService.findByToken(authorization).orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_TOKEN));

        // Make sure that the userId matches the id of the user obtained from the session
        if(!Objects.equals(id, session.getUser().getId())) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        final Map<Topic, Story> topStories = userService.getRealTimeTopStories(id);
        final TopStoriesModel topStoriesModel = new TopStoriesModel(topStories);
        return ResponseEntity.ok(topStoriesModel);
    }

    @RequestMapping(value ="/{id}/stories", method = RequestMethod.GET)
    public ResponseEntity<TopStoriesModel> getTopStories(@AuthorizationHeader @RequestHeader(AuthorizationHeader.AUTHORIZATION_HEADER) final String authorization,
                                                         @PathVariable final Long id) {

        // Should NOT occur, due to Authentication Aspect
        final Session session = sessionService.findByToken(authorization).orElseThrow(() -> new BusinessErrorException(BusinessError.INVALID_TOKEN));

        // Make sure that the userId matches the id of the user obtained from the session
        if(!Objects.equals(id, session.getUser().getId())) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        final Map<Topic, Story> topStories = userService.getTopStories(id);
        final TopStoriesModel topStoriesModel = new TopStoriesModel(topStories);
        return ResponseEntity.ok(topStoriesModel);
    }

}
