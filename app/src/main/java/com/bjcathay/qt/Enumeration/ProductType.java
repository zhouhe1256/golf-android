package com.bjcathay.qt.Enumeration;

import java.io.Serializable;

/**
 * Created by dengt on 15-7-31.
 */
public class ProductType implements Serializable{
    public enum prdtType {
        COMBO, DATE, TIME, REAL_TIME;
    }
    public enum payType {
        PREPAY, SPOTPAY, BLENDPAY;
    }
    public enum priceType {
        INACTIVE,ACTIVE,REAL_TIME;
    }
}
