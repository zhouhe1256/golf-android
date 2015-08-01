
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-5-5.
 */
public class StadiumListModel implements Serializable {
    @JSONCollection(type = StadiumModel.class)
    private List<StadiumModel> golfCourses;
    private boolean hasNext;
    public List<StadiumModel> getGolfCourses() {
        return golfCourses;
    }

    public void setGolfCourses(List<StadiumModel> golfCourses) {
        this.golfCourses = golfCourses;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    private static IContentDecoder<StadiumListModel> decoder = new IContentDecoder.BeanDecoder<StadiumListModel>(
            StadiumListModel.class);

    public static IPromise stadiums(int page) {
        return Http.instance().get(ApiUrl.STSDIUM_LIST).
                param("page", page).contentDecoder(decoder).run();
    }

    public static IPromise stadiums(int page, String lat, String lon, String cityId,
            String provinceId, String order) {
        return Http.instance().get(ApiUrl.STSDIUM_LIST).
                param("page", page).param("cityId", cityId).param("lat", lat).param("lon", lon)
                .param("provinceId", provinceId).param("order", order).contentDecoder(decoder)
                .run();
    }
}
