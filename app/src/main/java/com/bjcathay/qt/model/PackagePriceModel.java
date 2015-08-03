package com.bjcathay.qt.model;

import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.qt.Enumeration.ProductType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-8-3.
 */
public class PackagePriceModel implements Serializable{
    private String period;
    @JSONCollection(type = PriceJsonModel.class)
    private List<PriceJsonModel> items;
    private int minPerson;
    private int maxPerson;
    // 获取价格
    public int[] getFianlPrice(ProductType.prdtType prdtType, int number,
                               String hourSelect) {
        // 时间位于中间
        int[] priceStr = null;
        // if (DateUtil.CompareTime(date, startAt, endAt)) {
        // 处于时间段中
        switch (prdtType) {
            case DATE:
            case COMBO:
            case TIME:
                for (PriceJsonModel json : items) {
                    priceStr = json.getDetailPrice(number, prdtType, hourSelect);
                    if (priceStr[0] != 0)
                        break;
                }
                break;
            case REAL_TIME:
                break;
            // }
        }
        return priceStr;
    }
    public void getPersonsDuring(ProductType.prdtType prdtType) {
        switch (prdtType) {
            case DATE:
            case COMBO:
                setMinPerson(Integer.valueOf(items.get(0).getStart()));
                setMaxPerson(Integer.valueOf(items.get(items.size() - 1).getEnd()));
                break;
            case TIME:
                setMinPerson(1);
                setMaxPerson(99);
                break;
            case REAL_TIME:
                break;
        }
    }

    public int getMinPerson() {
        return minPerson;
    }

    public void setMinPerson(int minPerson) {
        this.minPerson = minPerson;
    }

    public int getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(int maxPerson) {
        this.maxPerson = maxPerson;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<PriceJsonModel> getItems() {
        return items;
    }

    public void setItems(List<PriceJsonModel> items) {
        this.items = items;
    }
}
