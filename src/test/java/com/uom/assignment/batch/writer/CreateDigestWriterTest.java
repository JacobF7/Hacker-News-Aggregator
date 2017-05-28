package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DigestService;
import com.uom.assignment.service.DurationType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * A test suite for {@link CreateDigestWriter}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestWriterTest {

    @Mock
    private Story mockStory;

    @Mock
    private Topic mockTopic;

    @Mock
    private Digest mockDigest;

    @Mock
    private TopStoryModel mockTopStoryModel;

    @Mock
    private DigestService digestService;

    @InjectMocks
    private CreateDigestWriter createDigestWriter;

    @Test
    public void write_createsDigest() {

        // Mocking mockUpdateTopStoriesModel to contain mockTopic and mockStory
        Mockito.when(mockTopStoryModel.getTopic()).thenReturn(mockTopic);
        Mockito.when(mockTopStoryModel.getStory()).thenReturn(mockStory);

        // Mocking that the mockDigest is returned
        Mockito.when(digestService.create(mockTopic, mockStory, DurationType.WEEKLY)).thenReturn(mockDigest);

        createDigestWriter.write(Collections.singletonList(mockTopStoryModel));

        // Verifying that a digest was created
        Mockito.verify(digestService).create(mockTopic, mockStory, DurationType.WEEKLY);
    }

}
