
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

/**
 * Created by dengt on 15-6-5.
 */
public class WXPayModel {
    private static IContentDecoder<?> contentDecoder = new IContentDecoder.BinaryDecoder();

    public static IPromise pay(Long id) {
        return Http.instance().post(ApiUrl.WX_ORDER_TO_PAY).contentDecoder(contentDecoder).
                param("id", id).run();
    }

    public static IPromise paytext(Long id,boolean isUseBalance,String type) {
        return Http.instance().post(ApiUrl.WX_ORDER_TO_PAY).
                param("id", id).param("isUseBalance", isUseBalance).param("type", type).run();
    }
    public  static IPromise rechargetext(Long id,String type){
        return Http.instance().post(ApiUrl.CHARGE_PAY).
                param("chargeId", id).param("type", type).run();
    }
}
