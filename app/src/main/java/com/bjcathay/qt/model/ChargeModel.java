package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-27.
 */
public class ChargeModel implements Serializable {
    private Long id;
    private static IContentDecoder<ChargeModel> decoder = new IContentDecoder.BeanDecoder<ChargeModel>(ChargeModel.class, "charge");

    public static IPromise OrderToPay(Long userOrderId, String type) {
        return Http.instance().post(ApiUrl.ORDER_TO_PAY).param("id", userOrderId).
                param("type", type).run();
    }
}
