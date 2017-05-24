package com.uom.assignment.hacker.news.api.service;

import com.uom.assignment.hacker.news.api.request.HackerNewsRequest;
import com.uom.assignment.hacker.news.api.response.HackerNewsResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * The Hacker News Rest API Services that submits a {@link HackerNewsRequest} and produces a {@link HackerNewsResponse}.
 *
 * Created by jacobfalzon on 16/05/2017.
 */
public interface HackerNewsApiService {

    /**
     * Submit a {@link HackerNewsRequest} of type {@link RequestMethod#GET}.
     *
     * @param request the submitted {@link HackerNewsRequest}.
     * @return the {@link HackerNewsResponse} produced from the response returned by the {@link HackerNewsRequest}.
     * @throws IOException if the an unexpected error occurs whilst reading the response.
     */
    HackerNewsResponse doGet(HackerNewsRequest request) throws IOException;
}
