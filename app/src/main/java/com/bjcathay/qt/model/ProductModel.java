package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-5-18.
 */
public class ProductModel implements Serializable {

    private Long id;//2,
    private String name;// "北京十三陵国际高尔夫"
    private double lat;// 46.213213,
    private double lon;//116.23213,
    private double price;// 200, //默认为当日价格，团购和特卖价格也是此字段
    private String priceInclude;// "18洞/车/餐",
    private String address;// "北京市朝阳区清河湾高尔夫俱乐部",
    private Long golfCourseId;// 1,//球场ID 用于进入球场详情
    private String imageUrl;// "/upload/stadium/2015/5/15/1431667509354-7367.jpg",
    private String type;// GROUP|SPECIAL|LIMIT|NONE //团购，特卖，最低起卖，无
    private String start;// "2015-05-05 09:00:00", //类型团购时存在
    private String end;// "2015-05-05 09:00:00", //类型团购时存在
    private String now;// "2015-05-05 09:00:00", //类型团购时存在
    private int amount;// "11", //类型 特卖和最低起卖时存在，表示特卖剩余数量和最低起卖数
    private String date;
    private String feature;
    private String bhStartAt;
    private String bhEndAt;
    @JSONCollection(type = PriceModel.class)
    private List<PriceModel> prices;// [ "/upload/image/xxx.png",
    private static IContentDecoder<ProductModel> decoder = new IContentDecoder.BeanDecoder<ProductModel>(ProductModel.class, "product");
    @JSONCollection(type = ActivityModel.class)
    private ActivityModel activity;// [ "/upload/image/xxx.png",

    public String getBhStartAt() {
        return bhStartAt;
    }

    public void setBhStartAt(String bhStartAt) {
        this.bhStartAt = bhStartAt;
    }

    public String getBhEndAt() {
        return bhEndAt;
    }

    public void setBhEndAt(String bhEndAt) {
        this.bhEndAt = bhEndAt;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceInclude() {
        return priceInclude;
    }

    public void setPriceInclude(String priceInclude) {
        this.priceInclude = priceInclude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getGolfCourseId() {
        return golfCourseId;
    }

    public void setGolfCourseId(Long golfCourseId) {
        this.golfCourseId = golfCourseId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<PriceModel> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceModel> prices) {
        this.prices = prices;
    }

    public ActivityModel getActivity() {
        return activity;
    }

    public void setActivity(ActivityModel activity) {
        this.activity = activity;
    }
    public static IPromise product(long id) {
        return Http.instance().get(ApiUrl.productDetail(id)).
                contentDecoder(decoder).run();
    }
}
