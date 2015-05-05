package com.bjcathay.golf.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by bjcathay on 15-5-5.
 */
public class BannerModel implements Serializable {
    private Long id;
    private String title;
    private String websiteUrl;
    private String imageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    private static IContentDecoder<BannerModel> decoder = new IContentDecoder.BeanDecoder<BannerModel>(BannerModel.class, "banner");

}
