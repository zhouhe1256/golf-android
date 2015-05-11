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
    private String type;//LIMITED|SPECIAL|COMMON, //限购，团购，平常
    private String description;// "描述",
    private String packageContent;// "内容",
    private String price;//88.8,
    private String address;//"北京市朝阳区",
    private double lon;// 40.544566,
    private double lat;// 116.565456,
    private String priceData;// "{"2015-05-05":200,...}",
    private int hole;// 18,
    private String holeNumber;//"18洞",
    private String lengthCode;// "全场总长",
    private String floorSpace;// "占地面积",
    private String startAt;// "09:00",
    private String endAt;// "18:00",
    private String  date;// "2015-10-23", //类型为LIMITED|SPECIAL 时存在
    private int intervalTime;//15,
    private int totalAmount;// 20,//总人数
    private int remainingAmount;// 10,//剩余人数

    @JSONCollection(type = String.class)
    private List<String> imageUrls;// [ "/upload/image/xxx.png",
    private String imageUrl;

    @JSONCollection(type = PriceModel.class)
    private List<PriceModel> prices;// [ "/upload/image/xxx.png",

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackageContent() {
        return packageContent;
    }

    public void setPackageContent(String packageContent) {
        this.packageContent = packageContent;
    }

    public int getHole() {
        return hole;
    }

    public void setHole(int hole) {
        this.hole = hole;
    }

    public String getLengthCode() {
        return lengthCode;
    }

    public void setLengthCode(String lengthCode) {
        this.lengthCode = lengthCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public List<PriceModel> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceModel> prices) {
        this.prices = prices;
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

    private static IContentDecoder<StadiumModel> decoder = new IContentDecoder.BeanDecoder<StadiumModel>(StadiumModel.class, "golfCourse");

    //场馆详情
    public static IPromise stadiumDetail(Long id) {
        return Http.instance().get(ApiUrl.stadiumDetail(id)).contentDecoder(decoder).
                run();
    }

}
