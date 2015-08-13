
package com.bjcathay.qt.model;

import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.util.DateUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-5-11.
 */
public class PriceModel implements Serializable {
    private Long id;// 1,
    private String startAt;// '2015-10-22 00:00:00',
    private String endAt;// '2015-10-23 00:00:00',
    private double price;//
    @JSONCollection(type = PriceJsonModel.class)
    private List<PriceJsonModel> priceJson;
    private String priceInclude;// "18洞/车/餐",
    private String status;// "INACTIVE",表示休息
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
                for (PriceJsonModel json : priceJson) {
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
                setMinPerson(Integer.valueOf(priceJson.get(0).getStart()));
                setMaxPerson(Integer.valueOf(priceJson.get(priceJson.size() - 1).getEnd()));
                break;
            case TIME:
                setMinPerson(1);
                setMaxPerson(99);
                break;
            case REAL_TIME:
                break;
        }
    }

    public boolean getAMTime(ProductType.prdtType prdtType, String hour) {
        switch (prdtType) {
            case TIME:
                for (PriceJsonModel json : priceJson) {
                    if (json.getTimeTrue(hour)) {
                        return true;
                    }
                }
                return false;
        }
        return false;
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

    // 待定
    private String priceType;
    private static IContentDecoder<PriceModel> decoder = new IContentDecoder.BeanDecoder<PriceModel>(
            PriceModel.class, "price");

    public List<PriceJsonModel> getPriceJson() {
        return priceJson;
    }

    public void setPriceJson(List<PriceJsonModel> priceJson) {
        this.priceJson = priceJson;
    }

    public String getPriceInclude() {
        return priceInclude;
    }

    public void setPriceInclude(String priceInclude) {
        this.priceInclude = priceInclude;
    }

    public ProductType.priceType getStatus() {
        return ProductType.priceType.valueOf(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

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
