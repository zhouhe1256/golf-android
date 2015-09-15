package com.bjcathay.qt.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by jiangm on 15-9-14.
 */
public class RechargeModel implements Serializable {
    private long id;
    private long chargeId;
    private int sum;
    private String rechargeway;
    private String title="充值订单";
    private String body="账户充值";

    private static IContentDecoder<RechargeModel> decoder = new IContentDecoder.BeanDecoder<RechargeModel>(
            RechargeModel.class, "recharge");

    public long getChargeId() {
        return chargeId;
    }

    public void setChargeId(long chargeId) {
        this.chargeId = chargeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getRechargeway() {
        return rechargeway;
    }

    public void setRechargeway(String rechargeway) {
        this.rechargeway = rechargeway;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
