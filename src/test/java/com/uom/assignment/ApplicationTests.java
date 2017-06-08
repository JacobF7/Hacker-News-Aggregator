package com.uom.assignment;

import com.google.common.collect.Sets;
import com.uom.assignment.cache.CacheConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * A test suite serving to verify that the Context loads.
 *
 * Created by jacobfalzon on 10/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void contextLoads() {
		// Verifying that all Caches are loaded
		Assert.assertEquals(cacheManager.getCacheNames(), Sets.newHashSet(CacheConfiguration.LATEST_DIGESTS_CACHE_KEY, CacheConfiguration.TOP_STORIES_CACHE_KEY, CacheConfiguration.TOPICS_CACHE_KEY));
	}

}
