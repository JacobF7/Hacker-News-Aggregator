package com.uom.assignment.controller;

import com.uom.assignment.aspect.AuthorizationHeader;
import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.model.request.DateRangeModel;
import com.uom.assignment.model.request.TopicModel;
import com.uom.assignment.model.request.UserModel;
import com.uom.assignment.model.response.TopStoriesModel;
import com.uom.assignment.model.response.UserDigestsModel;
import com.uom.assignment.model.response.UserTopicModel;
import com.uom.assignment.service.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserTopicModel> create(@RequestBody @Valid final UserModel userModel) {
        final Pair<Long, Set<String>> user = userService.create(userModel.getUsername(), userModel.getPassword(), userModel.getTopics());
        return ResponseEntity.ok(new UserTopicModel(user.getKey(), user.getValue()));
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<UserTopicModel> update(@AuthorizationHeader final HttpServletRequest request,
                                                 @PathVariable final Long id,
                                                 @RequestBody @Valid final TopicModel topicModel) {

        // Make sure that the id matches the USER_ID which was set in the request from the Authentication Aspect
        if(!Objects.equals(id, request.getAttribute(AuthorizationHeader.USER_ID))) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        userService.update(id, topicModel.getTopics());
        return ResponseEntity.ok(new UserTopicModel(id, topicModel.getTopics()));
    }

    @RequestMapping(value ="/{id}/stories", method = RequestMethod.GET)
    public ResponseEntity<TopStoriesModel> getTopStories(@AuthorizationHeader final HttpServletRequest request,
                                                         @PathVariable final Long id) {

        // Make sure that the id matches the USER_ID which was set in the request from the Authentication Aspect
        if(!Objects.equals(id, request.getAttribute(AuthorizationHeader.USER_ID))) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        final Map<Topic, Story> topStories = userService.getTopStories(id);
        final TopStoriesModel topStoriesModel = new TopStoriesModel(topStories);
        return ResponseEntity.ok(topStoriesModel);
    }

    @RequestMapping(value ="/{id}/digests", method = RequestMethod.GET)
    public ResponseEntity<UserDigestsModel> getDigests(@AuthorizationHeader final HttpServletRequest request,
                                                       @PathVariable final Long id,
                                                       @Valid final DateRangeModel dateRangeModel) {

        // Make sure that the id matches the USER_ID which was set in the request from the Authentication Aspect
        if(!Objects.equals(id, request.getAttribute(AuthorizationHeader.USER_ID))) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        final Map<LocalDate, List<Digest>> digests = userService.getDigests(id, dateRangeModel.getStart(), dateRangeModel.getEnd());
        final UserDigestsModel userDigestsModel = new UserDigestsModel(digests);
        return ResponseEntity.ok(userDigestsModel);
    }

    @RequestMapping(value ="/{id}/digests/latest", method = RequestMethod.GET)
    public ResponseEntity<UserDigestsModel> getLatestDigests(@AuthorizationHeader final HttpServletRequest request,
                                                             @PathVariable final Long id) {

        // Make sure that the id matches the USER_ID which was set in the request from the Authentication Aspect
        if(!Objects.equals(id, request.getAttribute(AuthorizationHeader.USER_ID))) {
            throw new BusinessErrorException(BusinessError.INVALID_TOKEN);
        }

        final Map<LocalDate, List<Digest>> digests = userService.getLatestDigests(id);
        final UserDigestsModel userDigestsModel = new UserDigestsModel(digests);
        return ResponseEntity.ok(userDigestsModel);
    }
}
