package com.bjcathay.qt.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.qt.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-25.
 */
public class ShareModel implements Serializable {
    private String title;// "7铁高尔夫",
    private String description;// "打球人都知道",
    private String smsContent;// "http://www.bjcathay.com",
    private String url;// "http://www.bjcathay.com"
    private static IContentDecoder<ShareModel> decoder = new IContentDecoder.BeanDecoder<ShareModel>(ShareModel.class, "share");

    public static IPromise share() {
        return Http.instance().get(ApiUrl.SHARE_APP).
                contentDecoder(decoder).run();
    }

    public static IPromise shareProducts(Long id) {
        return Http.instance().get(ApiUrl.SHARE_PRODUCTS).param("id", id).
                contentDecoder(decoder).run();
    }

    public static IPromise shareCompetitions(Long id) {
        return Http.instance().get(ApiUrl.SHARE_COMPETITION).param("id", id).
                contentDecoder(decoder).run();
    }

    public static IPromise shareOrders(Long id) {
        return Http.instance().get(ApiUrl.SHARE_ORDER).param("id", id).
                contentDecoder(decoder).run();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
