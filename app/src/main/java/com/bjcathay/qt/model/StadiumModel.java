
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-5-5.
 */
public class StadiumModel implements Serializable {

    private String mode;// "球场模式",
    private String designer;// "设计师",
    private String data;// "球场数据",
    private String area;// "球场面积",
    private String greenGrass;// "果岭草种",
    private String fairwayGrass;// "球道草种",
    private String length;// "球场长度",
    private String phone;// "球场电话",
    private String websiteUrl;// "球场网站",
    private Long id;// 1,
    private String name;// "名称",
    private String description;// "描述",
    private String address;// "北京市朝阳区",
    private double lon;// 40.544566,
    private double lat;// 116.565456,
    private String startAt;// "09:00", "开始营业时间",
    private String endAt;// "18:00","结束营业时间",
    private String date;// "建设日期",

    @JSONCollection(type = String.class)
    private List<String> imageUrls;// [ "/upload/image/xxx.png",
    private String imageUrl;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGreenGrass() {
        return greenGrass;
    }

    public void setGreenGrass(String greenGrass) {
        this.greenGrass = greenGrass;
    }

    public String getFairwayGrass() {
        return fairwayGrass;
    }

    public void setFairwayGrass(String fairwayGrass) {
        this.fairwayGrass = fairwayGrass;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    private static IContentDecoder<StadiumModel> decoder = new IContentDecoder.BeanDecoder<StadiumModel>(
            StadiumModel.class, "golfCourse");

    // 场馆详情
    public static IPromise stadiumDetail(Long id) {
        return Http.instance().get(ApiUrl.stadiumDetail(id)).contentDecoder(decoder).
                run();
    }

}
