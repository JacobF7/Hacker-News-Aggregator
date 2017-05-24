package com.uom.assignment.model.response;

import com.uom.assignment.dao.Story;
import com.uom.assignment.dao.Topic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A model containing a {@link TopStoryModel} for each {@link Topic#name}.
 *
 * Created by jacobfalzon on 21/05/2017.
 */
public class TopStoriesModel {

    private final Map<String, TopStoryModel> topStories;

    public TopStoriesModel(final Map<Topic, Story> topStories) {

        this.topStories = topStories.entrySet()
                                   .stream()
                                   .collect(HashMap::new,
                                            (stories, entry) -> stories.put(entry.getKey().getName(), TopStoryModel.of(entry.getValue())),
                                            HashMap::putAll);
    }

    public Map<String, TopStoryModel> getTopStories() {
        return topStories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final TopStoriesModel that = (TopStoriesModel) o;
        return Objects.equals(getTopStories(), that.getTopStories());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopStories());
    }
}
