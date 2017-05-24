package com.uom.assignment.cache;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Value("${cache.expiry.time.minutes}")
    private long expiry;

    @Bean
    public CacheManager cacheManager() {
        final GuavaCacheManager guavaCacheManager =  new GuavaCacheManager(TOPICS_CACHE_KEY);
        guavaCacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterAccess(expiry, TimeUnit.MINUTES));
        return guavaCacheManager;
    }
}