package com.uom.assignment.batch.reader;

import java.util.List;

/**
 * The mode with which a {@link List} of Stories is to be fetched.
 *
 * Created by jacobfalzon on 22/05/2017.
 */
public enum FetchMode {

    /**
     * Retrieves all persisted stories.
     */
    ALL,

    /**
     * Retrieves all recently updated stories
     */
    RECENT
}
