package com.uom.assignment.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * The {@link Configuration} for the {@link CacheManager}.
 * Note that the Cache expiry time is configurable from properties file.
 *
 * Created by jacobfalzon on 13/05/2017.
 */
@Configuration
public class CacheConfiguration {

    public static final String TOPICS_CACHE_KEY = "topics";
    public static final String LATEST_DIGESTS_CACHE_KEY = "latest_digests";
    public static final String TOP_STORIES_CACHE_KEY = "top_stories";

    @Value("${topics.cache.expiry.time.minutes}")
    private long topicsExpiry;

    @Value("${digests.cache.expiry.time.days}")
    private long digestsExpiry;

    @Value("${stories.cache.expiry.time.minutes}")
    private long storiesExpiry;

    @Bean
    public CacheManager cacheManager() {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();

        final GuavaCache topicsCache = new GuavaCache(TOPICS_CACHE_KEY, CacheBuilder.newBuilder().expireAfterAccess(topicsExpiry, TimeUnit.MINUTES).build());
        final GuavaCache latestDigestsCache = new GuavaCache(LATEST_DIGESTS_CACHE_KEY, CacheBuilder.newBuilder().expireAfterAccess(digestsExpiry, TimeUnit.DAYS).build());
        final GuavaCache topStoriesCache = new GuavaCache(TOP_STORIES_CACHE_KEY, CacheBuilder.newBuilder().expireAfterAccess(storiesExpiry, TimeUnit.MINUTES).build());

        cacheManager.setCaches(Arrays.asList(topicsCache, latestDigestsCache, topStoriesCache));

        return cacheManager;
    }
}