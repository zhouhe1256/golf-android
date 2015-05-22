package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-11.
 */
public class OrderListModel implements Serializable {
    @JSONCollection(type = OrderModel.class)
    private List<OrderModel> orders;

    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }

    private static IContentDecoder<OrderListModel> decoder = new IContentDecoder.BeanDecoder<OrderListModel>(OrderListModel.class);

    public static IPromise getMyOrder(int page) {
        return Http.instance().get(ApiUrl.MY_ORDERS).
                param("page", page).
                contentDecoder(decoder).run();
    }
}
