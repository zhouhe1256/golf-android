
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.util.List;

/**
 * Created by dengt on 15-6-25.
 */
public class ProvinceListModel {
    @JSONCollection(type = ProvinceModel.class)
    private List<ProvinceModel> provinces;

    public List<ProvinceModel> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceModel> provinces) {
        this.provinces = provinces;
    }

    private static IContentDecoder<ProvinceListModel> decoder = new IContentDecoder.BeanDecoder<ProvinceListModel>(
            ProvinceListModel.class);

    public static IPromise getProvince() {
        return Http.instance().get(ApiUrl.PROVINCES).
                contentDecoder(decoder).run();
    }
}
