
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.JSONUtil;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.Enumeration.ProductType;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-5-18.
 */
public class ProductModel implements Serializable {

    private String name;// "北京十三陵国际高尔夫"
    private String tags; // "特卖"
    private Long golfCourseId;// 1,//球场ID 用于进入球场详情
    private String imageUrl;// "/upload/stadium/2015/5/15/1431667509354-7367.jpg",
    private String now;// "2015-05-05 09:00:00",
    private String purchasingNotice;
    private String type;// "COMBO"|"DATE"|"TIME"|"REAL_TIME",//套餐,日期,时间,实时
    private String payType;// "PREPAY"|"SPOTPAY"|"BLENDPAY"|,
    private double price;// 200, //默认为当日价格
    private Long id;// 2,
    private String description;
    private String priceInclude;// "18洞/车/餐",
    private String address;// "北京市朝阳区清河湾高尔夫俱乐部",
    private String recommendReason;
    private double lat;// 46.213213,
    private double lon;// 116.23213,
    private String costInclude;
    private String feature;
    private String scheduling;
    private String amountNotice;
    private int intervalTime;
    @JSONCollection(type = PriceModel.class)
    private List<PriceModel> prices;// [ "/upload/image/xxx.png",
    private String priceJson;
    private PackagePriceModel packagePriceModel;
    // 待定

    private String start;// "2015-05-05 09:00:00", //类型团购时存在
    private String end;// "2015-05-05 09:00:00", //类型团购时存在
    private int amount;// "11", //类型 特卖和最低起卖时存在，表示特卖剩余数量和最低起卖数
    private String date;

    private String bhStartAt;
    private String bhEndAt;
    private String label;
    private double distance;
    private String priceType;// NORMAL|REST|REAL_TIME 正常|休息|实时

    private static IContentDecoder<ProductModel> decoder = new IContentDecoder.BeanDecoder<ProductModel>(
            ProductModel.class, "product");

    public String getPriceJson() {
        return priceJson;
    }

    public PackagePriceModel getPackagePriceModel() {
        //String json = priceJson.replaceAll("", "\\");
        packagePriceModel = JSONUtil.load(PackagePriceModel.class, priceJson);
        return packagePriceModel;
    }

    public void setPackagePriceModel(PackagePriceModel packagePriceModel) {
        String json = priceJson.replaceAll("", "\\");
        packagePriceModel = JSONUtil.load(PackagePriceModel.class, json);
        this.packagePriceModel = packagePriceModel;
    }

    public void setPriceJson(String priceJson) {
        this.priceJson = priceJson;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public String getCostInclude() {
        return costInclude;
    }

    public void setCostInclude(String costInclude) {
        this.costInclude = costInclude;
    }

    public String getScheduling() {
        return scheduling;
    }

    public void setScheduling(String scheduling) {
        this.scheduling = scheduling;
    }

    public String getAmountNotice() {
        return amountNotice;
    }

    public void setAmountNotice(String amountNotice) {
        this.amountNotice = amountNotice;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String[] getTagsType() {
        if (tags != null) {
        String[] tagType = tags.split(",");
        return tagType; }
        else
            return null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tags;
    }

    public void setTag(String tag) {
        this.tags = tag;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPurchasingNotice() {
        return purchasingNotice;
    }

    public void setPurchasingNotice(String purchasingNotice) {
        this.purchasingNotice = purchasingNotice;
    }

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

    public ProductType.prdtType getType() {
        return ProductType.prdtType.valueOf(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProductType.payType getPayType() {
        return ProductType.payType.valueOf(payType);
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public static IPromise product(long id) {
        return Http.instance().get(ApiUrl.productDetail(id)).
                run();
    }
}
