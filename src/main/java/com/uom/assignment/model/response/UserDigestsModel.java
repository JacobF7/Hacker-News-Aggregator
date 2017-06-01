package com.uom.assignment.model.response;

import com.uom.assignment.dao.Digest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The model containing a {@link UserDigestModel} for every {@link Digest#creationDate}.
 *
 * Created by jacobfalzon on 30/05/2017.
 */
public class UserDigestsModel {

    private final Map<String, UserDigestModel> userDigests;

    public UserDigestsModel(final Map<LocalDate, List<Digest>> digests) {

        this.userDigests = digests.entrySet()
                .stream()
                .collect(HashMap::new,
                         (stories, entry) -> stories.put(entry.getKey().toString(), UserDigestModel.of(entry.getValue())),
                         HashMap::putAll);
    }

    public Map<String, UserDigestModel> getUserDigests() {
        return userDigests;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final UserDigestsModel that = (UserDigestsModel) o;
        return Objects.equals(getUserDigests(), that.getUserDigests());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserDigests());
    }

}
