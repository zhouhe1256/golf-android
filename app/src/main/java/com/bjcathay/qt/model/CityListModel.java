
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-6-25.
 */
public class CityListModel implements Serializable {
    @JSONCollection(type = GetCitysModel.class)
    private List<GetCitysModel> cities;

    public void setCities(List<GetCitysModel> cities) {
        this.cities = cities;
    }

    public List<GetCitysModel> getCities() {
        return cities;
    }

    private static IContentDecoder<CityListModel> decoder = new IContentDecoder.BeanDecoder<CityListModel>(
            CityListModel.class);

    public static IPromise getTotalCities() {
        return Http.instance().get(ApiUrl.CITIES).
                contentDecoder(decoder).run();
    }
}
