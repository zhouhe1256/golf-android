
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
public class BannerListModel implements Serializable {
    @JSONCollection(type = BannerModel.class)
    private List<BannerModel> banners;

    public List<BannerModel> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerModel> banners) {
        this.banners = banners;
    }

    private static IContentDecoder<BannerListModel> decoder = new IContentDecoder.BeanDecoder<BannerListModel>(
            BannerListModel.class);

    public static IPromise getHomeBanners() {
        return Http.instance().get(ApiUrl.HOME_BANNER).
                contentDecoder(decoder).run();
    }
}
