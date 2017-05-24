package com.uom.assignment.hacker.news.api.request;

import com.uom.assignment.hacker.news.api.core.HackerNewsEndpoint;
import com.uom.assignment.hacker.news.api.core.ResponseContentType;
import com.uom.assignment.hacker.news.api.response.ItemResponse;

/**
 * The {@link HackerNewsRequest} that serves to retrieve all details related a particular item, such as a Story.
 * Note that this request returns a response containing a {@link ResponseContentType#JSON_OBJECT}.
 *
 * Created by jacobfalzon on 17/05/2017.
 */
public class ItemRequest extends AbstractHackerNewsRequest {

    public ItemRequest(final Long id) {
        super(HackerNewsEndpoint.ITEM.getEndpoint() + id + HackerNewsEndpoint.JSON_RESPONSE_SUFFIX,
              ItemResponse.class);
    }

    @Override
    public ResponseContentType getResponseContentType() {
        return ResponseContentType.JSON_OBJECT;
    }
}
