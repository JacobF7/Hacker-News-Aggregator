package com.uom.assignment.batch.writer;

import com.uom.assignment.dao.Story;
import com.uom.assignment.model.request.UpdateStoryModel;
import com.uom.assignment.service.StoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * A test suite for {@link UpdateStoriesWriter}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateStoriesWriterTest {

    @Mock
    private Story oldStory;

    @Mock
    private Story updatedStory;

    @Mock
    private UpdateStoryModel mockUpdateStoryModel;

    @Mock
    private StoryService storyService;

    @InjectMocks
    private UpdateStoriesWriter updateStoriesWriter;

    private static final Long SCORE = 1L;

    @Test
    public void write_scoreChanged_updatesScore() {

        // Mocking mockUpdateStoryModel to contain mockStory, SCORE and deleted as false
        Mockito.when(mockUpdateStoryModel.getStory()).thenReturn(oldStory);
        Mockito.when(mockUpdateStoryModel.getScore()).thenReturn(SCORE);
        Mockito.when(mockUpdateStoryModel.isDeleted()).thenReturn(false);

        // Mocking that updatedStory is returned on update
        Mockito.when(storyService.update(oldStory, SCORE)).thenReturn(updatedStory);

        updateStoriesWriter.write(Collections.singletonList(mockUpdateStoryModel));

        // Verifying that mockStory was updated
        Mockito.verify(storyService).update(oldStory, SCORE);

        // Verifying that mockStory was not deleted
        Mockito.verify(storyService, Mockito.never()).delete(oldStory);
    }

    @Test
    public void write_storyDeleted_deletesStories() {

        // Mocking mockUpdateStoryModel to contain mockStory, SCORE and deleted as true
        Mockito.when(mockUpdateStoryModel.getStory()).thenReturn(oldStory);
        Mockito.when(mockUpdateStoryModel.getScore()).thenReturn(null);
        Mockito.when(mockUpdateStoryModel.isDeleted()).thenReturn(true);

        // Mocking that updatedStory is returned on delete
        Mockito.when(storyService.delete(oldStory)).thenReturn(updatedStory);

        updateStoriesWriter.write(Collections.singletonList(mockUpdateStoryModel));

        // Verifying that mockStory was deleted
        Mockito.verify(storyService).delete(oldStory);

        // Verifying that mockStory was not updated
        Mockito.verify(storyService, Mockito.never()).update(Matchers.eq(oldStory), Matchers.anyLong());
    }

}
