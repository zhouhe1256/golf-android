package com.bjcathay.golf.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-11.
 */
public class PriceModel implements Serializable {
    private Long id;// 1,
    private String startAt;//'2015-10-22 00:00:00',
    private String endAt;//'2015-10-23 00:00:00',
    private double price;// 199
    private static IContentDecoder<PriceModel> decoder = new IContentDecoder.BeanDecoder<PriceModel>(PriceModel.class, "price");

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
