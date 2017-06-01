package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;
import com.uom.assignment.dao.User;
import com.uom.assignment.model.request.CreateDigestModel;
import com.uom.assignment.model.request.TopStoryModel;
import com.uom.assignment.service.DigestService;
import com.uom.assignment.service.DurationType;
import com.uom.assignment.service.StoryService;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

/**
 * A test suite for {@link CreateDigestWriter}.
 *
 * Created by jacobfalzon on 27/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateDigestWriterTest {

    @Mock
    private Story mockFirstStory;

    @Mock
    private Story mockSecondStory;

    @Mock
    private Topic mockFirstTopic;

    @Mock
    private Topic mockSecondTopic;

    @Mock
    private User mockFirstUser;

    @Mock
    private User mockSecondUser;

    @Mock
    private Digest mockFirstDigest;

    @Mock
    private Digest mockSecondDigest;

    @Mock
    private CreateDigestModel mockFirstCreateDigestModel;

    @Mock
    private CreateDigestModel mockSecondCreateDigestModel;

    @Mock
    private DigestService digestService;

    @Mock
    private StoryService storyService;

    @InjectMocks
    private CreateDigestWriter createDigestWriter;

    @Test
    public void write_createsTopicDigest_createsOverallDigest() {

        // Mocking mockFirstTopicTopStoryModel to contain mockFirstTopic, mockFirstStory and mockFirstUser
        Mockito.when(mockFirstCreateDigestModel.getTopic()).thenReturn(mockFirstTopic);
        Mockito.when(mockFirstCreateDigestModel.getStory()).thenReturn(mockFirstStory);
        Mockito.when(mockFirstCreateDigestModel.getUsers()).thenReturn(Collections.singleton(mockFirstUser));

        // Mocking mockSecondTopicTopStoryModel to contain mockSecondTopic, mockSecondStory, mockFirstUser and mockSecondUser
        Mockito.when(mockSecondCreateDigestModel.getTopic()).thenReturn(mockSecondTopic);
        Mockito.when(mockSecondCreateDigestModel.getStory()).thenReturn(mockSecondStory);
        Mockito.when(mockSecondCreateDigestModel.getUsers()).thenReturn(Sets.newLinkedHashSet(mockFirstUser, mockSecondUser));

        // Mocking that the mockFirstDigest is returned
        Mockito.when(digestService.createTopicDigest(mockFirstTopic, mockFirstStory, Collections.singleton(mockFirstUser), DurationType.WEEKLY)).thenReturn(mockFirstDigest);

        // Mocking that the mockSecondDigest is returned
        Mockito.when(digestService.createTopicDigest(mockSecondTopic, mockSecondStory, Sets.newLinkedHashSet(mockFirstUser, mockSecondUser), DurationType.WEEKLY)).thenReturn(mockSecondDigest);

        createDigestWriter.write(Arrays.asList(Collections.singletonList(mockFirstCreateDigestModel), Collections.singletonList(mockSecondCreateDigestModel)));

        // Verifying that a digest was created for mockFirstTopic, mockFirstStory and mockFirstUser
        Mockito.verify(digestService).createTopicDigest(mockFirstTopic, mockFirstStory, Collections.singleton(mockFirstUser), DurationType.WEEKLY);

        // Verifying that a digest was created for mockSecondTopic and mockSecondStory, mockFirstUser and mockSecondUser
        Mockito.verify(digestService).createTopicDigest(mockSecondTopic, mockSecondStory, Sets.newLinkedHashSet(mockFirstUser, mockSecondUser), DurationType.WEEKLY);
    }

}
