package com.uom.assignment.hacker.news.api.service;

import com.uom.assignment.hacker.news.api.request.ItemRequest;
import com.uom.assignment.hacker.news.api.request.NewStoriesRequest;
import com.uom.assignment.hacker.news.api.response.EmptyResponse;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;
import com.uom.assignment.hacker.news.api.response.ItemResponse;
import com.uom.assignment.hacker.news.api.response.NewStoriesResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * A test suite for {@link HackerNewsApiService}.
 *
 * Created by jacobfalzon on 19/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class HackerNewsApiServiceImplTest {

    @InjectMocks
    private HackerNewsApiServiceImpl hackerNewsApiService;

    private static final Long ITEM_ID = 1L;
    private static final Long INVALID_ITEM_ID = -1L;

    @Test
    public void doGet_newStoriesRequest_returnsNewStoriesResponse() throws Exception {
        final HackerNewsResponse hackerNewsResponse = hackerNewsApiService.doGet(new NewStoriesRequest());

        Assert.assertNotNull(hackerNewsResponse);
        Assert.assertEquals(NewStoriesResponse.class, hackerNewsResponse.getClass());

        final NewStoriesResponse newStoriesResponse = (NewStoriesResponse) hackerNewsResponse;
        Assert.assertNotNull(newStoriesResponse.getItems());
    }

    @Test
    public void doGet_itemRequest_returnItemResponse() throws Exception {
        final HackerNewsResponse hackerNewsResponse = hackerNewsApiService.doGet(new ItemRequest(ITEM_ID));

        Assert.assertNotNull(hackerNewsResponse);
        Assert.assertEquals(ItemResponse.class, hackerNewsResponse.getClass());

        final ItemResponse newStoriesResponse = (ItemResponse) hackerNewsResponse;
        Assert.assertNotNull(newStoriesResponse.getId());
        Assert.assertNotNull(newStoriesResponse.getBy());
        Assert.assertNotNull(newStoriesResponse.getScore());
        Assert.assertNotNull(newStoriesResponse.getTime());
        Assert.assertNotNull(newStoriesResponse.getTitle());
        Assert.assertNotNull(newStoriesResponse.getType());
        Assert.assertNotNull(newStoriesResponse.getUrl());
    }

    @Test
    public void doGet_itemRequest_invalidRequest_returnEmptyResponse() throws Exception {
        final HackerNewsResponse hackerNewsResponse = hackerNewsApiService.doGet(new ItemRequest(INVALID_ITEM_ID));

        Assert.assertNotNull(hackerNewsResponse);
        Assert.assertEquals(EmptyResponse.class, hackerNewsResponse.getClass());
    }

}