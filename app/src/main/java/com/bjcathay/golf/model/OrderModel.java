package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.golf.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-11.
 */
public class OrderModel implements Serializable {

    private Long id;// 1,
    private String orderId;//"1000xxxxxxx"
    private String title;//"标题",
    private String packageContent;// "18洞/餐/车",
    private String startAt;// "2015-08-08 09:00:00",
    private String createdAt;// "2015-08-08 09:00:00",
    private double totalPrice;// 800.0,
    private double unitPrice;// 200.0,
    private int peopleNumber;//4, // 人数
    private String expiredCountdown;// "600000",//单位 毫秒
    private boolean expired;// true|false,
    private String expiredTime;//'2015-10-22 19:00:00',
    private String status;//订单状态标识,PENDING|PROCESSING|UNPAID|PAID|FINISH|CANCEL 待确认 确认中 待支付 已支付 已完成 已取消
    private String imageUrl;// "/upload/image/xxx.png"
    private String type;

    @JSONCollection(type = StadiumModel.class)
    private StadiumModel golfCourse;
    private static IContentDecoder<OrderModel> decoder = new IContentDecoder.BeanDecoder<OrderModel>(OrderModel.class, "order");

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = golfCourse.getType();
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

    public String getPackageContent() {
        return packageContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setPackageContent(String packageContent) {
        this.packageContent = packageContent;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public StadiumModel getGolfCourse() {
        return golfCourse;
    }

    public void setGolfCourse(StadiumModel golfCourse) {
        this.golfCourse = golfCourse;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
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

    public String getExpiredCountdown() {
        return expiredCountdown;
    }

    public void setExpiredCountdown(String expiredCountdown) {
        this.expiredCountdown = expiredCountdown;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
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
                contentDecoder(decoder).
                run();
    }

    public static IPromise orderDelete(Long id) {
        return Http.instance().post(ApiUrl.orderDelete(id)).
                param("_method", "DELETE").
                contentDecoder(decoder).run();
    }

    public static IPromise commitOrder(Long id, int count, String date) {
        return Http.instance().post(ApiUrl.COMMIT_ORDER).
                param("golfCourseId", id).
                param("count", count).
                param("date", date).
                contentDecoder(decoder).run();
    }

    public static IPromise orderVerify(Long id) {
        return Http.instance().get(ApiUrl.orderVerify(id)).
                contentDecoder(decoder).run();
    }
}
