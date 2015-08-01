package com.bjcathay.qt.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-7-31.
 */
public class PriceJsonListModel implements Serializable{
    @JSONCollection(type = PriceJsonModel.class)
    private List<PriceJsonModel> priceJson;

    public List<PriceJsonModel> getPriceJson() {
        return priceJson;
    }

    public void setPriceJson(List<PriceJsonModel> priceJson) {
        this.priceJson = priceJson;
    }
}
