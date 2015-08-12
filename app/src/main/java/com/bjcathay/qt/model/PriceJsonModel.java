
package com.bjcathay.qt.model;

import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.util.DateUtil;

import java.io.Serializable;

/**
 * Created by dengt on 15-7-31.
 */
public class PriceJsonModel implements Serializable {
    private String start;
    private String end;
    private int price;
    private int fan;
    private int prePay;
    private int spotPay;

    public int[] getDetailPrice(int number,ProductType.prdtType prdtType,String hourSelect) {
        int[] priceStr=new int[]{0,0,0,0};
        switch (prdtType){
            case DATE:
            case COMBO:
                if(number>=Integer.valueOf(start) && number<=Integer.valueOf(end)){
                    priceStr[0]=price;
                    priceStr[1]=fan;
                    priceStr[2]=prePay;
                    priceStr[3]=spotPay;
                }
                break;
            case TIME:
                if(DateUtil.CompareShortTime(hourSelect,start,end)){
                    priceStr[0]=price;
                    priceStr[1]=fan;
                    priceStr[2]=prePay;
                    priceStr[3]=spotPay;
                }
                break;
            case REAL_TIME:
                break;
        }
        return priceStr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFan() {
        return fan;
    }

    public void setFan(int fan) {
        this.fan = fan;
    }

    public int getPrePay() {
        return prePay;
    }

    public void setPrePay(int prePay) {
        this.prePay = prePay;
    }

    public int getSpotPay() {
        return spotPay;
    }

    public void setSpotPay(int spotPay) {
        this.spotPay = spotPay;
    }


}
