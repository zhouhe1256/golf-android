package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-6-25.
 */
public class GolfCourseListModel implements Serializable {
    @JSONCollection(type = GolfCourseModel.class)
    private List<GolfCourseModel> golfCourses;

    public List<GolfCourseModel> getGolfCourses() {
        return golfCourses;
    }
    public void setGolfCourses(List<GolfCourseModel> golfCourses) {
        this.golfCourses = golfCourses;
    }

    private static IContentDecoder<GolfCourseListModel> decoder = new IContentDecoder.BeanDecoder<GolfCourseListModel>(GolfCourseListModel.class);
    public static IPromise searchGolf() {
        return Http.instance().get(ApiUrl.GOLF_RESEARCH).
                contentDecoder(decoder).run();
    }
}
