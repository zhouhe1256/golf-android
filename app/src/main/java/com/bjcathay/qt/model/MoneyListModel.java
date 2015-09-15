package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-9-14.
 */
public class MoneyListModel implements Serializable {
    @JSONCollection(type = MoneyModel.class)
    private List<MoneyModel> chargeList;

    public List<MoneyModel> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<MoneyModel> chargeList) {
        this.chargeList = chargeList;
    }

    private static IContentDecoder<MoneyListModel> decoder = new IContentDecoder.BeanDecoder<MoneyListModel>(
            MoneyListModel.class);

    public static IPromise getCharge() {
        return Http.instance().get(ApiUrl.MONEIES).
                contentDecoder(decoder).run();
    }
}
