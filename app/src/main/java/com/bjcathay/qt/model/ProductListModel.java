
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-5-18.
 */
public class ProductListModel implements Serializable {
    public List<ProductModel> getProducts() {
        return products;
    }

    private boolean hasNext;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    @JSONCollection(type = ProductModel.class)
    private List<ProductModel> products;
    private String now;

    private static IContentDecoder<ProductListModel> decoder = new IContentDecoder.BeanDecoder<ProductListModel>(
            ProductListModel.class);

    public static IPromise productList(int page, String lat, String lon, String cityId) {
        return Http.instance().get(ApiUrl.PRODUCT_LIST).
                param("page", page).param("cityId", cityId).param("lat", lat).param("lon", lon)
                .contentDecoder(decoder)
                .run();
    }

    public static IPromise productList(long id, int page, String lat, String lon, String cityId) {
        return Http.instance().get(ApiUrl.productList(id)).
                param("page", page).param("cityId", cityId).param("lat", lat).param("lon", lon)
                .contentDecoder(decoder)
                .run();
    }

    public static IPromise comboList(int page, String lat, String lon, String cityId,
                                     String provinceId,String order) {
        return Http.instance().get(ApiUrl.COMBO_LIST).
                param("page", page).param("cityId", cityId).param("lat", lat).param("lon", lon)
                .param("provinceId", provinceId).param("order", order).contentDecoder(decoder)
                .run();
    }

    public static IPromise searchProduct(String cityId, String golfCourseId, String lat, String lon) {
        return Http.instance().get(ApiUrl.PRODUCT_RESEARCH).
                param("cityId", cityId).param("golfCourseId", golfCourseId).param("lat", lat)
                .param("lon", lon).contentDecoder(decoder).run();
    }
}
