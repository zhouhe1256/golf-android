package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

/**
 * Created by bjcathay on 15-6-5.
 */
public class WXPayModel {
    private static IContentDecoder<?> contentDecoder = new IContentDecoder.BinaryDecoder();

    public static IPromise pay(Long id) {
        return Http.instance().post(ApiUrl.WX_ORDER_TO_PAY).contentDecoder(contentDecoder).
                param("id", id).run();
    }
    public static IPromise paytext(Long id) {
        return Http.instance().post(ApiUrl.WX_ORDER_TO_PAY).
                param("id", id).run();
    }
}
