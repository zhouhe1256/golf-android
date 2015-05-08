package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.golf.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-5.
 */
public class StadiumModel implements Serializable {
    private Long id;// 1,
    private String name;// "名称",
    private String description;// "描述",
    private String comboContent;// "内容",
    private String price;//88.8,
    private String address;//"北京市朝阳区",
    private double lon;// 40.544566,
    private double lat;// 116.565456,
    private String priceData;// "{"2015-05-05":200,...}",
    private String holeNumber;//"18洞",
    private String fullLength;// "全场总长",
    private String floorSpace;// "占地面积",
    private String startAt;// "09:00",
    private String endAt;// "18:00",
    private int intervalTime;//15,

    @JSONCollection(type = String.class)
    private List<String> imageUrls;// [ "/upload/image/xxx.png",
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComboContent() {
        return comboContent;
    }

    public void setComboContent(String comboContent) {
        this.comboContent = comboContent;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getPriceData() {
        return priceData;
    }

    public void setPriceData(String priceData) {
        this.priceData = priceData;
    }

    public String getHoleNumber() {
        return holeNumber;
    }

    public void setHoleNumber(String holeNumber) {
        this.holeNumber = holeNumber;
    }

    public String getFullLength() {
        return fullLength;
    }

    public void setFullLength(String fullLength) {
        this.fullLength = fullLength;
    }

    public String getFloorSpace() {
        return floorSpace;
    }

    public void setFloorSpace(String floorSpace) {
        this.floorSpace = floorSpace;
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

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    private static IContentDecoder<StadiumModel> decoder = new IContentDecoder.BeanDecoder<StadiumModel>(StadiumModel.class, "stadium");

    //预约与否
    public static IPromise stadiumDetail(Long id) {
        return Http.instance().get(ApiUrl.stadiumDetail(id)).contentDecoder(decoder).
                run();
    }

}
