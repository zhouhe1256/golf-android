package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-6.
 */
public class EventListModel implements Serializable {

    @JSONCollection(type = EventModel.class)
    private List<EventModel> events;
    private boolean hasNext;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<EventModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
    }

    private static IContentDecoder<EventListModel> decoder = new IContentDecoder.BeanDecoder<EventListModel>(EventListModel.class);

    public static IPromise get(int page) {
        return Http.instance().get(ApiUrl.EVENT_LIST).
                contentDecoder(decoder).
                param("page", page).run();
    }
    public static IPromise getMyEvent(int page) {
        return Http.instance().get(ApiUrl.MY_EVENT).
                contentDecoder(decoder).
                param("page", page).run();
    }
}
