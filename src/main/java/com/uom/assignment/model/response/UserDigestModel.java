package com.uom.assignment.model.response;

import com.uom.assignment.dao.Digest;
import com.uom.assignment.dao.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A model containing all the {@link Digest}s associated to a {@link User} for a particular {@link Digest#creationDate}.
 *
 * Created by jacobfalzon on 30/05/2017.
 */
public class UserDigestModel {

    private Map<String, StoryModel> topicDigests;
    private List<StoryModel> overallDigests;

    private UserDigestModel(final Map<String, StoryModel> topicDigests, final List<StoryModel> overallDigests) {
        this.topicDigests = topicDigests;
        this.overallDigests= overallDigests;
    }

    public Map<String, StoryModel> getTopicDigestModel() {
        return topicDigests;
    }

    public void setTopicDigestModel(final Map<String, StoryModel> topicDigests) {
        this.topicDigests = topicDigests;
    }

    public List<StoryModel> getOverallDigestModel() {
        return overallDigests;
    }

    public void setOverallDigestModel(final List<StoryModel> overallDigests) {
        this.overallDigests = overallDigests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UserDigestModel that = (UserDigestModel) o;

        return Objects.equals(getTopicDigestModel(), that.getTopicDigestModel()) &&
                Objects.equals(getOverallDigestModel(), that.getOverallDigestModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTopicDigestModel(), getOverallDigestModel());
    }

    public static UserDigestModel of(final List<Digest> digests) {

        final Map<String, StoryModel> topicDigests =
                digests.stream()
                       .filter(digest -> !digest.getOverall())
                       .collect(Collectors.toMap(digest -> digest.getTopic().getName(), digest -> StoryModel.of(digest.getStory())));

        final List<StoryModel> overallDigests =
                digests.stream()
                       .filter(Digest::getOverall)
                       .map(Digest::getStory)
                       .map(StoryModel::of)
                       .collect(Collectors.toList());

        return new UserDigestModel(topicDigests, overallDigests);
    }
}
