package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
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
    private String comboContent;// "18洞/餐/车",
    private String startAt;// "2015-08-08 09:00:00",
    private double totalPrice;// 800.0,
    private double unitPrice;// 200.0,
    private int peopleNumber;//4, // 人数
    private String expiredCountdown;// "600000",//单位 毫秒
    private boolean expired;// true|false,
    private String status;//订单状态标识,PENDING|PROCESSING|UNPAID|PAID|FINISH|CANCEL
    private String imageUrl;// "/upload/image/xxx.png"
    private static IContentDecoder<OrderModel> decoder = new IContentDecoder.BeanDecoder<OrderModel>(OrderModel.class, "order");

    public Long getId() {
        return id;
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

    public String getComboContent() {
        return comboContent;
    }

    public void setComboContent(String comboContent) {
        this.comboContent = comboContent;
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
                contentDecoder(decoder).run();
    }

    public static IPromise orderDelete(Long id) {
        return Http.instance().delete(ApiUrl.orderDelete(id)).
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
