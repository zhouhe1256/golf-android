package com.bjcathay.golf.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.golf.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-4-30.
 */
public class EventModel implements Serializable {
    private Long id;
    private String name;//赛事名称
    private String description;
    private String type;
    private String address;
    private String imageUrl;
    private String conductDate;//举办时间
    private String startAt;//报名开始时间
    private String endAt;//报名结束时间
    private int signUpAmount;//可报名人数
    private int signedAmount;//已报名人数
    private String websiteUrl;
    private String resultStatus;
    private String status;


    public enum Type {
        PUBLIC, INVITATION
    }

    public enum ResultStatus {
        SELECTION, NOT_PASS, PASS
    }

    public enum Status {
        SIGNING, FINISH, CANCEL
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getConductDate() {
        return conductDate;
    }

    public void setConductDate(String conductDate) {
        this.conductDate = conductDate;
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

    public int getSignUpAmount() {
        return signUpAmount;
    }

    public void setSignUpAmount(int signUpAmount) {
        this.signUpAmount = signUpAmount;
    }

    public int getSignedAmount() {
        return signedAmount;
    }

    public void setSignedAmount(int signedAmount) {
        this.signedAmount = signedAmount;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private static IContentDecoder<EventModel> decoder = new IContentDecoder.BeanDecoder<EventModel>(EventModel.class, "event");


    public static IPromise getEventDetail(long id) {
        return Http.instance().get(ApiUrl.eventDetail(id)).
                contentDecoder(decoder).
                run();
    }

    public static IPromise attendEvent(long id) {
        return Http.instance().post(ApiUrl.attendEvent(id)).

                run();
    }

    public static IPromise deleteEvent(long id) {
        return Http.instance().post(ApiUrl.DELETE_EVENTS).
                param("_method", "DELETE").
                param("from", id).
                run();
    }

    public static IPromise deleteEvent(List<String> ids) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < ids.size(); i++) {
            if (i < ids.size() - 1)
                buffer.append(ids.get(i) + ",");
            else
                buffer.append(ids.get(i));
        }
        return Http.instance().delete(ApiUrl.DELETE_EVENTS).
                param("from", buffer).run();
    }
}
