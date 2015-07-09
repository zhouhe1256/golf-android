
package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-5-11.
 */
public class OrderModel implements Serializable {

    private Long id;// 1,
    private String orderId;// "1000xxxxxxx"
    private String title;// "标题",
    private String priceInclude;// "18洞/餐/车",
    private String date;// "2015-08-08 09:00:00",//预约时间
    private double totalPrice;// 800.0,
    private double unitPrice;// 200.0,
    private int peopleNumber;// 4, // 人数
    private String createdAt;// "2015-08-08 09:00:00",
    private String type;// GROUP|SPECIAL|LIMIT|NONE, //团购，特卖，最低起卖，无
    private String expiredTime;// '2015-10-22 19:00:00',仅在订单未支付状态下存在
    private String now;// '2015-10-22 19:00:00',仅在订单未支付状态下存在
    private String status;// 订单状态标识,PENDING|PROCESSING|UNPAID|PAID|FINISH|CANCEL
                          // 待确认 确认中 待支付 已支付 已完成 已取消
    private String imageUrl;// "/upload/image/xxx.png"
    private String mobileNumber;
    private double lat;// 46.213213,
    private double lon;// 116.23213,
    private String address;// "2015-08-08 09:00:00",
    private String userRealName;
    private String personNames;
    private String purchasingNotice;

    private static IContentDecoder<OrderModel> decoder = new IContentDecoder.BeanDecoder<OrderModel>(
            OrderModel.class, "order");

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getPersonNames() {
        return personNames;
    }

    public void setPersonNames(String personNames) {
        this.personNames = personNames;
    }

    public String getPurchasingNotice() {
        return purchasingNotice;
    }

    public void setPurchasingNotice(String purchasingNotice) {
        this.purchasingNotice = purchasingNotice;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public String getPriceInclude() {
        return priceInclude;
    }

    public void setPriceInclude(String priceInclude) {
        this.priceInclude = priceInclude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static IPromise orderDetail(Long id) {
        return Http.instance().get(ApiUrl.orderDetail(id)).
                contentDecoder(decoder).run();
    }

    public static IPromise orderCancle(Long id) {
        return Http.instance().put(ApiUrl.orderCancle(id)).
                run();
    }

    public static IPromise orderDelete(Long id) {
        return Http.instance().post(ApiUrl.orderDelete()).
                param("_method", "DELETE").
                param("from", id).
                run();
    }

    public static IPromise commitOrder(Long id, int count, String date) {
        return Http.instance().post(ApiUrl.COMMIT_ORDER).
                param("productId", id).
                param("count", count).
                param("date", date).
                run();
    }

    /*
     * name: 联系人姓名 mobileNumber: 联系人电话 playBallPerson: 打球人 JSON格式字符串 不许为空，参考以下格式
     */
    public static IPromise commitNewOrder(Long id, int count, String date, String name,
            String mobileNumber,
            String playBallPerson) {
        return Http.instance().post(ApiUrl.COMMIT_ORDER).
                param("productId", id).
                param("count", count).
                param("date", date).
                param("name", name).
                param("mobileNumber", mobileNumber).
                param("playBallPerson", playBallPerson).
                run();
    }

    public static IPromise orderVerify(Long id) {
        return Http.instance().get(ApiUrl.orderVerify(id)).
                run();
    }
}
