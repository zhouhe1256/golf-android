
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
    private List<StadiumModel> goods;

    public List<StadiumModel> getGoods() {
        return goods;
    }

    public void setGoods(List<StadiumModel> goods) {
        this.goods = goods;
    }

    private static IContentDecoder<StadiumListModel> decoder = new IContentDecoder.BeanDecoder<StadiumListModel>(
            StadiumListModel.class);

    public static IPromise stadiums(int page) {
        return Http.instance().get(ApiUrl.STSDIUM_LIST).
                param("page", page).contentDecoder(decoder).run();
    }
}
