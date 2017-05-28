package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.TopicService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * A test suite for {@link UpdateTopStoriesWriterTest}.
 *
 * Created by jacobfalzon on 24/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateTopStoriesWriterTest {

    @Mock
    private Story mockStory;

    @Mock
    private Topic mockTopic;

    @Mock
    private Topic updatedTopic;

    @Mock
    private TopStoryModel mockTopStoryModel;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private UpdateTopStoriesWriter updateTopStoriesWriter;

    @Test
    public void write_updatesTopStory() {

        // Mocking mockTopStoryModel to contain mockTopic and mockStory
        Mockito.when(mockTopStoryModel.getTopic()).thenReturn(mockTopic);
        Mockito.when(mockTopStoryModel.getStory()).thenReturn(mockStory);

        // Mocking that the updatedTopic is returned on update
        Mockito.when(topicService.update(mockTopic, mockStory)).thenReturn(updatedTopic);

        updateTopStoriesWriter.write(Collections.singletonList(mockTopStoryModel));

        // Verifying that mockTopic was updated
        Mockito.verify(topicService).update(mockTopic, mockStory);
    }
}
