package com.uom.assignment.batch.reader;

import com.uom.assignment.dao.Topic;
import com.uom.assignment.service.TopicService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * A test suite for {@link CreateDigestReader}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestReaderTest {

    @Mock
    private Topic mockTopic;

    @Mock
    private TopicService topicService;

    @Test
    public void read_topicsDoNotExist_returnsNull() throws Exception {

        // Mocking that no topic exists
        Mockito.when(topicService.findAll()).thenReturn(Collections.emptyList());

        final CreateDigestReader reader = new CreateDigestReader(topicService);

        // Verifying that null is returned
        Assert.assertNull(reader.read());
    }

    @Test
    public void read_topicExists_returnsMockTopic() throws Exception {

        // Mocking that a topic exists
        Mockito.when(topicService.findAll()).thenReturn(Collections.singletonList(mockTopic));

        final CreateDigestReader reader = new CreateDigestReader(topicService);

        // Verifying that a topic returned
        Assert.assertEquals(reader.read(), mockTopic);
    }
}
