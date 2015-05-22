package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-18.
 */
public class ProductListModel implements Serializable {
    public List<ProductModel> getProducts() {
        return products;
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

    private static IContentDecoder<ProductListModel> decoder = new IContentDecoder.BeanDecoder<ProductListModel>(ProductListModel.class);
    public static IPromise productList(int page) {
        return Http.instance().get(ApiUrl.PRODUCT_LIST).
                param("page", page).contentDecoder(decoder).run();
    }
}
